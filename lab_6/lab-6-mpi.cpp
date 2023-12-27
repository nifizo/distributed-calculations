#include <iostream>
#include <mpi.h>
#include "mpi_matrix_operations.h"

int main(int argc, char* argv[]) {
    srand(time(0));
    setvbuf(stdout, 0, _IONBF, 0);
    MPI_Init(&argc, &argv);

    MPI_Comm_rank(MPI_COMM_WORLD, &proc_rank);
    MPI_Comm_size(MPI_COMM_WORLD, &proc_num);

    switch (proc_num) {
    case 1:
        mpi_matrix::runTest(argc, argv, 150);
        std::cout << "______________________________________________\n";
        mpi_matrix::runTest(argc, argv, 300);
        std::cout << "______________________________________________\n";
        mpi_matrix::runTest(argc, argv, 600);
        std::cout << "______________________________________________\n";
        mpi_matrix::runTest(argc, argv, 1200);
        std::cout << "______________________________________________\n";
        break;

    case 4:
        mpi_matrix::runTest(argc, argv, 144);
        std::cout << "______________________________________________\n";
        mpi_matrix::runTest(argc, argv, 288);
        std::cout << "______________________________________________\n";
        mpi_matrix::runTest(argc, argv, 576);
        std::cout << "______________________________________________\n";
        mpi_matrix::runTest(argc, argv, 1152);
        std::cout << "______________________________________________\n";
        break;

    case 9:
        mpi_matrix::runTest(argc, argv, 162);
        std::cout << "______________________________________________\n";
        mpi_matrix::runTest(argc, argv, 324);
        std::cout << "______________________________________________\n";
        mpi_matrix::runTest(argc, argv, 648);
        std::cout << "______________________________________________\n";
        mpi_matrix::runTest(argc, argv, 1296);
        std::cout << "______________________________________________\n";
        break;

    default:
        std::cout << "\n Invalid number of processes!";
    }

    MPI_Finalize();
    return 0;
}