package org.example;

import java.security.SecureRandom;

public class Garden {
    public Integer[][] gardenMap;

    public Garden(int rows, int cols){
        gardenMap = new Integer[rows][cols];
        SecureRandom random = new SecureRandom();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                gardenMap[i][j] = random.nextInt(3);
            }
        }
    }

    public void start(){
        Nature nature = new Nature(this);
        Gardener gardener = new Gardener(this);
        ConsoleLogger logger = new ConsoleLogger(this);
        FileLogger f_logger = new FileLogger(this);
        nature.start();
        gardener.start();
        logger.start();
        f_logger.start();
    }
}