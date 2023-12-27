#pragma once

#include <iostream>
#include <random>
#include <time.h>
#include <math.h>
#include <mpi.h>


static int proc_num = 0;
static int proc_rank = 0;
static MPI_Comm col_comm;
static MPI_Comm row_comm;

namespace mpi_matrix {

	namespace base {
		double* RandomDataInitialization(int size) {
			int matrix_size = size * size;
			double* matrix = new double[matrix_size];
			for (int i = 0; i < matrix_size; i++) {
				matrix[i] = (rand() % (5));
			}
			return matrix;
		}


		void simpleMultiplication(double* pAMatrix, double*& pBMatrix, double*& pCMatrix, int size) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					for (int k = 0; k < size; k++) {
						pCMatrix[i * size + j] += pAMatrix[i * size + k] * pBMatrix[k * size + j];
					}
				}
			}
		}

		void TransposeMatrix(double* matrix, int size) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					double t = matrix[i * size + j];
					matrix[i * size + j] = matrix[j * size + i];
					matrix[j * size + i] = t;
				}
			}
		}

	}

	namespace block_striped {
		int coords;

		void Decomposition(double* line_B, int length, int size) {
			MPI_Status status;
			int next_coord = coords + 1;
			if (next_coord == proc_num) next_coord = 0;
			int PrevCoord = coords - 1;
			if (PrevCoord == -1) PrevCoord = proc_num - 1;
			MPI_Sendrecv_replace(line_B, length * size, MPI_DOUBLE, next_coord, 0, PrevCoord, 0, col_comm, &status);
		}

		void BlockStripedMultiplication(double* line_a, double* line_b, double* result, int size, int length, int iter) {
			int l = length * iter;
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < length; j++) {
					for (int k = 0; k < size; k++) {
						result[length * iter] += line_a[i * size + k] * line_b[j * size + k];
					}
					l++;
				}
				l += size - length;
			}
		}

		void ComputeLine(double* line_a, double* line_b, double* line_c, int length, int size) {
			int iter = coords;
			for (int i = 0; i < proc_num; i++) {
				BlockStripedMultiplication(line_a, line_b, line_c, size, length, iter);
				iter++;
				if (iter == proc_num) {
					iter = 0;
				}
				Decomposition(line_b, length, size);
			}
		}

		void ProcessInitialization(double*& pAMatrix, double*& pBMatrix, double*& pCMatrix, double*& pAblock, double*& pBblock, double*& pCblock, int& size, int& block_size) {
			block_size = size / proc_num;

			pAblock = new double[block_size * block_size];
			pBblock = new double[block_size * block_size];
			pCblock = new double[block_size * block_size];

			if (proc_rank == 0) {
				pAMatrix = new double[size * size];
				pBMatrix = new double[size * size];
				pCMatrix = new double[size * size];
				pAMatrix = base::RandomDataInitialization(size);
				pBMatrix = base::RandomDataInitialization(size);
			}
		}

		void  DataDistribution(double* A, double* B, double* line_ad, double* line_bd, int size, int length) {
			if (proc_rank == 0) {
				base::TransposeMatrix(B, size);
			}
			MPI_Scatter(&(A[size * length * coords]), length * size, MPI_DOUBLE, line_ad, length * size, MPI_DOUBLE, 0, row_comm);
			MPI_Scatter(&(B[size * length * coords]), length * size, MPI_DOUBLE, line_bd, length * size, MPI_DOUBLE, 0, row_comm);

		}

		void ResultCollection(double* C, double* LineCd, int length, int size) {
			MPI_Gather(LineCd, length * size, MPI_DOUBLE, C, length * size, MPI_DOUBLE, 0, row_comm);
		}

		void Deconstruct(double*& pAMatrix, double*& pBMatrix, double*& pCMatrix, double* pAblock, double* pBblock, double* pCblock, double* pTemporaryAblock = NULL) {
			if (proc_rank == 0) {
				delete[] pAMatrix;
				delete[] pBMatrix;
				delete[] pCMatrix;
			}
			delete[] pAblock;
			delete[] pBblock;
			delete[] pCblock;
			if (!pTemporaryAblock) {
				delete[] pTemporaryAblock;
			}
		}

		void runBlockStripedAlg(int argc, char* argv[], int size) {
			double* pAMatrix;
			double* pBMatrix;
			double* pCMatrix;
			int length;
			double* pAline;
			double* pBline;
			double* pCline;

			double Start, Finish, Duration;

			coords = proc_rank;
			if (size % proc_num != 0) {
				if (proc_rank == 0) {
					std::cout << "\n Invalid number of proesses for multiplication of matrices of these Sizes";
				}
				return;
			}
			ProcessInitialization(pAMatrix, pBMatrix, pCMatrix, pAline, pBline, pCline, size, length);
			MPI_Comm_split(MPI_COMM_WORLD, proc_rank / length, proc_rank, &row_comm);
			MPI_Comm_split(MPI_COMM_WORLD, proc_rank / length, proc_rank, &col_comm);

			Start = MPI_Wtime();
			DataDistribution(pAMatrix, pBMatrix, pAline, pBline, size, length);
			ComputeLine(pAline, pBline, pCline, length, size);
			Finish = MPI_Wtime();
			ResultCollection(pCMatrix, pCline, length, size);
			Deconstruct(pAMatrix, pBMatrix, pCMatrix, pAline, pBline, pCline);
			Duration = Finish - Start;
			if (proc_rank == 0) {
				std::cout << "Block-Striped " << Duration << std::endl;
			}
		}
	}

	namespace fox {
		int grid_coords[2];
		int grid_size;
		MPI_Comm grid_comm;
		MPI_Comm col_comm;
		MPI_Comm row_comm;


		void createGridCommunicators() {
			int size_dim[2];
			int periodic[2];
			int sub_dims[2];

			size_dim[0] = grid_size;
			size_dim[1] = grid_size;
			periodic[0] = 0;
			periodic[1] = 0;

			MPI_Cart_create(MPI_COMM_WORLD, 2, size_dim, periodic, 1, &grid_comm);
			MPI_Cart_coords(grid_comm, proc_rank, 2, grid_coords);

			sub_dims[0] = 0;
			sub_dims[1] = 1;
			MPI_Cart_sub(grid_comm, sub_dims, &row_comm);

			sub_dims[0] = 1;
			sub_dims[1] = 0;
			MPI_Cart_sub(grid_comm, sub_dims, &col_comm);
		}

		void ProcessInitialization(double*& pAMatrix, double*& pBMatrix, double*& pCMatrix, double*& pAblock, double*& pBblock, double*& pCblock, double*& pTemporaryAblock, int& size, int& block_size) {
			block_size = size / grid_size;

			pAblock = new double[block_size * block_size];
			pBblock = new double[block_size * block_size];
			pCblock = new double[block_size * block_size];
			pTemporaryAblock = new double[block_size * block_size];

			if (proc_rank == 0) {
				pAMatrix = new double[size * size];
				pBMatrix = new double[size * size];
				pCMatrix = new double[size * size];
				pAMatrix = base::RandomDataInitialization(size);
				pBMatrix = base::RandomDataInitialization(size);
			}
		}

		void DataDistributionMatrices(double* matrix, double* matrix_block, int size, int block_size) {
			double* Row = new double[block_size * size];
			if (grid_coords[1] == 0) {
				MPI_Scatter(matrix, block_size * size, MPI_DOUBLE, Row, block_size * size, MPI_DOUBLE, 0, col_comm);
			}
			for (int i = 0; i < block_size; i++) {
				MPI_Scatter(&Row[i * size], block_size, MPI_DOUBLE, &(matrix_block[i * block_size]), block_size, MPI_DOUBLE, 0, row_comm);
			}
			delete[] Row;
		}

		void DataDistribution(double* pAMatrix, double* pBMatrix, double* pAblock, double* pBblock, int size, int block_size) {
			DataDistributionMatrices(pAMatrix, pAblock, size, block_size);
			DataDistributionMatrices(pBMatrix, pBblock, size, block_size);
		}

		void ABlockCommunication(int iter, double* pAblock, double* pMatrixAblock, int block_size) {
			int Pivot = (grid_coords[0] + iter) % grid_size;
			if (grid_coords[1] == Pivot) {
				for (int i = 0; i < block_size * block_size; i++)
					pAblock[i] = pMatrixAblock[i];
			}
			MPI_Bcast(pAblock, block_size * block_size, MPI_DOUBLE, Pivot, row_comm);
		}

		void BblockCommunication(double* pBblock, int block_size) {
			MPI_Status status;
			int next_proc = grid_coords[0] + 1;
			if (grid_coords[0] == grid_size - 1) next_proc = 0;
			int  prev_proc = grid_coords[0] - 1;
			if (grid_coords[0] == 0)  prev_proc = grid_size - 1;
			MPI_Sendrecv_replace(pBblock, block_size * block_size, MPI_DOUBLE, next_proc, 0, prev_proc, 0, col_comm, &status);
		}

		void ParallelResultCalculation(double* pAblock, double* pMatrixAblock,
			double* pBblock, double* pCblock, int block_size) {
			for (int i = 0; i < grid_size; i++) {
				ABlockCommunication(i, pAblock, pMatrixAblock, block_size);
				base::simpleMultiplication(pAblock, pBblock, pCblock, block_size);
				BblockCommunication(pBblock, block_size);
			}
		}

		void ResultCollection(double* pCMatrix, double* pCblock, int size, int block_size, int grid_coords[]) {
			double* result = new double[size * block_size];
			for (int i = 0; i < block_size; i++) {
				MPI_Gather(&pCblock[i * block_size], block_size, MPI_DOUBLE, &result[i * size], block_size, MPI_DOUBLE, 0, row_comm);
			}
			if (grid_coords[1] == 0) {
				MPI_Gather(result, block_size * size, MPI_DOUBLE, pCMatrix, block_size * size, MPI_DOUBLE, 0, col_comm);
			}
			delete[] result;
		}

		void Deconstruct(double*& pAMatrix, double*& pBMatrix, double*& pCMatrix, double* pAblock, double* pBblock, double* pCblock, double* pTemporaryAblock = NULL) {
			if (proc_rank == 0) {
				delete[] pAMatrix;
				delete[] pBMatrix;
				delete[] pCMatrix;
			}
			delete[] pAblock;
			delete[] pBblock;
			delete[] pCblock;
			if (!pTemporaryAblock) {
				delete[] pTemporaryAblock;
			}
		}

		void runFoxAlg(int argc, char* argv[], int size) {
			double* pAMatrix;
			double* pBMatrix;
			double* pCMatrix;
			int block_size;
			double* pAblock;
			double* pBblock;
			double* pCblock;
			double* pMatrixAblock;
			double Start, Finish, Duration;

			grid_size = sqrt((double)proc_num);
			if (proc_num != grid_size * grid_size) {
				if (proc_rank == 0) {
					std::cout << "\nNumber of processes must be a perfect square \n";
				}
				return;
			}

			createGridCommunicators();
			ProcessInitialization(pAMatrix, pBMatrix, pCMatrix, pAblock, pBblock, pCblock, pMatrixAblock, size, block_size);
			DataDistribution(pAMatrix, pBMatrix, pMatrixAblock, pBblock, size, block_size);

			Start = MPI_Wtime();
			ParallelResultCalculation(pAblock, pMatrixAblock, pBblock,
				pCblock, block_size);
			Finish = MPI_Wtime();
			ResultCollection(pCMatrix, pCblock, size, block_size, grid_coords);
			Deconstruct(pAMatrix, pBMatrix, pCMatrix, pAblock, pBblock, pCblock, pAblock);

			Duration = Finish - Start;
			if (proc_rank == 0)
				std::cout << "fox " << Duration << std::endl;
		}
	}

	namespace cannon {
		int grid_coords[2];
		int grid_size;
		MPI_Comm grid_comm;
		MPI_Comm col_comm;
		MPI_Comm row_comm;

		void Deconstruct(double*& pAMatrix, double*& pBMatrix, double*& pCMatrix, double* pAblock, double* pBblock, double* pCblock, double* pTemporaryAblock = NULL) {
			if (proc_rank == 0) {
				delete[] pAMatrix;
				delete[] pBMatrix;
				delete[] pCMatrix;
			}
			delete[] pAblock;
			delete[] pBblock;
			delete[] pCblock;
			if (!pTemporaryAblock) {
				delete[] pTemporaryAblock;
			}
		}

		void ProcessInitialization(double*& pAMatrix, double*& pBMatrix, double*& pCMatrix, double*& pAblock, double*& pBblock, double*& pCblock, int& size, int& block_size) {
			block_size = size / grid_size;

			pAblock = new double[block_size * block_size];
			pBblock = new double[block_size * block_size];
			pCblock = new double[block_size * block_size];


			if (proc_rank == 0) {
				pAMatrix = new double[size * size];
				pBMatrix = new double[size * size];
				pCMatrix = new double[size * size];
				pAMatrix = base::RandomDataInitialization(size);
				pBMatrix = base::RandomDataInitialization(size);
			}
		}

		void ResultCollection(double* pCMatrix, double* pCblock, int size, int block_size, int grid_coords[]) {
			double* result = new double[size * block_size];
			for (int i = 0; i < block_size; i++) {
				MPI_Gather(&pCblock[i * block_size], block_size, MPI_DOUBLE, &result[i * size], block_size, MPI_DOUBLE, 0, row_comm);
			}
			if (grid_coords[1] == 0) {
				MPI_Gather(result, block_size * size, MPI_DOUBLE, pCMatrix, block_size * size, MPI_DOUBLE, 0, col_comm);
			}
			delete[] result;
		}

		void createGridCommunicators() {
			int DimSize[2];
			int Periodic[2];
			int SubDims[2];

			DimSize[0] = grid_size;
			DimSize[1] = grid_size;
			Periodic[0] = 0;
			Periodic[1] = 0;

			MPI_Cart_create(MPI_COMM_WORLD, 2, DimSize, Periodic, 1, &grid_comm);
			MPI_Cart_coords(grid_comm, proc_rank, 2, grid_coords);

			SubDims[0] = 0;
			SubDims[1] = 1;
			MPI_Cart_sub(grid_comm, SubDims, &row_comm);

			SubDims[0] = 1;
			SubDims[1] = 0;
			MPI_Cart_sub(grid_comm, SubDims, &col_comm);
		}

		void ABlockCommunication(double* pAMatrix, int size, int block_size) {
			int NextCoord = grid_coords[1] + 1;
			if (grid_coords[1] == grid_size - 1) NextCoord = 0;
			int PrevCoord = grid_coords[1] - 1;
			if (grid_coords[1] == 0) PrevCoord = grid_size - 1;
			MPI_Status status;
			MPI_Sendrecv_replace(pAMatrix, block_size * block_size, MPI_DOUBLE, NextCoord, 0, PrevCoord, 0, row_comm, &status);
		}

		void BBlockCommunication(double* pBMatrix, int size, int block_size) {
			MPI_Status status;
			int next_proc = grid_coords[0] + 1;
			if (grid_coords[0] == grid_size - 1) next_proc = 0;
			int prev_proc = grid_coords[0] - 1;
			if (grid_coords[0] == 0) prev_proc = grid_size - 1;
			MPI_Sendrecv_replace(pBMatrix, block_size * block_size, MPI_DOUBLE, next_proc, 0, prev_proc, 0, col_comm, &status);
		}

		void ParallelResultCalculation(double* pAMatrix, double* pBMatrix, double* pCMatrix, int size, int block_size) {
			for (int i = 0; i < grid_size; ++i) {
				base::simpleMultiplication(pAMatrix, pBMatrix, pCMatrix, block_size);
				ABlockCommunication(pAMatrix, size, block_size);
				BBlockCommunication(pBMatrix, size, block_size);
			}
		}

		void  DataDistributionBlock(double* Matrix, double* Block, int Row, int Col, int size, int block_size) {
			int Position = Col * block_size * size + Row * block_size;
			for (int i = 0; i < block_size; ++i, Position += size) {
				MPI_Scatter(&Matrix[Position], block_size, MPI_DOUBLE, &(Block[i * block_size]), block_size, MPI_DOUBLE, 0, grid_comm);
			}
		}

		void  DataDistribution(double* pAMatrix, double* pBMatrix, double* pAblock, double* pBblock, int size, int block_size) {
			DataDistributionBlock(pAMatrix, pAblock, grid_coords[0], (grid_coords[0] + grid_coords[1]) % grid_size, size, block_size);
			DataDistributionBlock(pBMatrix, pBblock, (grid_coords[0] + grid_coords[1]) % grid_size, grid_coords[1], size, block_size);
		}

		void runCannonAlg(int argc, char* argv[], int size) {
			double* pAMatrix;
			double* pBMatrix;
			double* pCMatrix;
			int block_size;
			double* pAblock;
			double* pBblock;
			double* pCblock;
			double* pMatrixAblock;
			double Start, Finish, Duration;

			grid_size = sqrt((double)proc_num);
			if (proc_num != grid_size * grid_size) {
				if (proc_rank == 0) {
					std::cout << "\nThe number of processes should be a perfect square \n";
				}
				return;
			}

			createGridCommunicators();
			ProcessInitialization(pAMatrix, pBMatrix, pCMatrix, pAblock, pBblock, pCblock, size, block_size);
			DataDistribution(pAMatrix, pBMatrix, pAblock, pBblock, size, block_size);

			Start = MPI_Wtime();
			ParallelResultCalculation(pAblock, pBblock, pCblock, size, block_size);
			Finish = MPI_Wtime();
			ResultCollection(pCMatrix, pCblock, size, block_size, grid_coords);
			Deconstruct(pAMatrix, pBMatrix, pCMatrix, pAblock, pBblock, pCblock, pAblock);

			Duration = Finish - Start;
			if (proc_rank == 0)
				std::cout << "cannon " << Duration << std::endl;
		}
	}

	void runTest(int argc, char* argv[], int size) {

		fox::runFoxAlg(argc, argv, size);
		cannon::runCannonAlg(argc, argv, size);
		block_striped::runBlockStripedAlg(argc, argv, size);
	}
};
