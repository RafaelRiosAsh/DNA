/**
 *from The nature of code
 * An exploration into a basic genetic algorithm
 *
 * @author Rafael rios
 *
 * basic genetic algorithm that approximates a given string
 * not very efficient
 */

import java.util.ArrayList;
import java.util.Random;

public class DNA {
    private char[] genes;
    private float fitness;
    private String genesInStr;

    DNA(String target) {
        genes = new char[target.length()];
        for (int i = 0; i < genes.length; i++) {
            Random randValue = new Random();
            int charValue = randValue.nextInt(97) + 32;
            genes[i] = (char) (charValue);
        }
    }

    public void setFitness(String target) {
        int score = 0;
        for (int i = 0; i < genes.length; i++) {
            if (genes[i] == target.charAt(i)) {
                score++;
            }
        }
        fitness = (float)score;
    }

    public float getFitness() {
        return fitness;
    }

    public void setGenes(int range, char newGene) {
        genes[range] = newGene;
    }

    public char getGenes(int range) {
        return genes[range];
    }

    public DNA crossover(DNA partner, String target) {
        DNA child = new DNA(target);

        Random midpointRand = new Random();
        int midpoint = midpointRand.nextInt(genes.length);

        for (int i = 0; i < genes.length; i++) {
            if (i > midpoint) child.setGenes(i, genes[i]);
            else child.setGenes(i, partner.getGenes(i));

        }
        return child;
    }

    public void mutate(double mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            Random mutationRandom = new Random();
            double mutationProb = mutationRandom.nextDouble();
            //System.out.println(mutationProb);
            if (mutationProb < mutationRate) {
                Random randGene = new Random();
                int newChar = randGene.nextInt(97) + 32;
                genes[i] = (char) newChar;
            }
        }
    }


    public String getGenesStr(){
        String genesInStr = new String(genes);
        return genesInStr;
    }

}

class Main{
    public static void main(String[] args) {

        DNA population[] = new DNA[300];
        String target = "banana bean";
        double mutationRate = 0.01;
        boolean notFound = true;
        int generations = 0;
        String perfectGenome="";


        for (int i = 0; i < population.length; i++){
            population[i] = new DNA(target);

        }

        while (notFound) {
            for (int i = 0; i < population.length; i++) {
                population[i].setFitness(target);
            }

            ArrayList<DNA> matingPool = new ArrayList<>();
            for (int i = 0; i < population.length; i++) {
                int n = (int) population[i].getFitness() * 10;

                for (int j = 0; j < n; j++) {
                    matingPool.add(population[i]);
                }
            }

            for (int i = 0; i < population.length; i++) {
                System.out.println(population[i].getGenesStr()+" "+population[i].getFitness());
                Random matingRand = new Random();
                int a = matingRand.nextInt(matingPool.size());
                int b = matingRand.nextInt(matingPool.size());

                DNA parentA = matingPool.get(a);
                DNA parentB = matingPool.get(b);

                DNA child = parentA.crossover(parentB, target);

                child.mutate(mutationRate);
                population[i] = child;

            }

            generations++;

            for (int i = 0; i < population.length; i++) {
                if (population[i].getGenesStr().equals(target)) {
                    perfectGenome = population[i].getGenesStr();
                    notFound = false;
                    break;
                }
            }
            System.out.println("------------");

        }
        System.out.println("done");
        System.out.println("genome: "+perfectGenome);
        System.out.println("generations: "+generations);
    }
}
