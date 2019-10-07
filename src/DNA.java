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

//DNA class is the item or object, it contains a string of characters
public class DNA {

    private char[] genes;
    private float fitness;
    private String genesInStr;

    //constructor that initializes by filling each slot in the array with a random character
    DNA(String target) {
        genes = new char[target.length()];
        for (int i = 0; i < genes.length; i++) {
            Random randValue = new Random();
            int charValue = randValue.nextInt(97) + 32;
            genes[i] = (char) (charValue);
        }
    }

    /*setFitness checks how many characters are the same and in the same position as the target string and it gives a
    * score based on how many characters match, this score will be used later to determine mating probability*/
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

    //used for every generation except the first to override the original random characters
    public void setGenes(int range, char newGene) {
        genes[range] = newGene;
    }

    public char getGenes(int range) {
        return genes[range];
    }

    //when the DNA object is going to mix with another one to produce an offspring it calls the crossover function
    public DNA crossover(DNA partner, String target) {
        //first it creates a child
        DNA child = new DNA(target);

        //then it uses a random number to decide how many characters to take from each parent
        Random midpointRand = new Random();
        int midpoint = midpointRand.nextInt(genes.length);

        //this array overrides the random characters that the child object had at the beginning
        for (int i = 0; i < genes.length; i++) {
            if (i > midpoint) child.setGenes(i, genes[i]);
            else child.setGenes(i, partner.getGenes(i));

        }
        //returns the child object that should be closer to the answer and a new generation
        return child;
    }

    //in order to prevent all the offsprings from being the same there is a change that the child will mutate
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

        //an array of DNA objects with a defined size
        DNA population[] = new DNA[300];
        //the target string that the system is trying to evolve towards
        String target = "banana bean";
        //mutation probability
        double mutationRate = 0.01;
        boolean notFound = true;
        int numOfGenerations = 0;
        String perfectGenome="";

        //creates the initial population of random characters attempting to get close to the target string
        for (int i = 0; i < population.length; i++){
            population[i] = new DNA(target);

        }

        //while loop to repeat the code until the perfect match is found
        while (notFound) {
            //using the setFitness function, each DNA object is assigned a fitness grade
            for (int i = 0; i < population.length; i++) {
                population[i].setFitness(target);
            }

            //an arraylist is created
            ArrayList<DNA> matingPool = new ArrayList<>();
            /*it runs through every DNA object in the population array and it gets its fitness number and multiplies it
            by 10, the higher the fitness level the more DNA object copies of itself that will be inside the arraylist*/
            for (int i = 0; i < population.length; i++) {
                int n = (int) population[i].getFitness() * 10;

                for (int j = 0; j < n; j++) {
                    matingPool.add(population[i]);
                }
            }

            /*this for runs through the mating pool arraylist and gets two random parents, thats why the fitness level
            is important, the bigger the fitness level the higher the probability that it will be picked and pass
            its genes on to the next generation*/
            for (int i = 0; i < population.length; i++) {
                System.out.println(population[i].getGenesStr()+" "+population[i].getFitness());
                Random matingRand = new Random();
                //picking two random parents
                int a = matingRand.nextInt(matingPool.size());
                int b = matingRand.nextInt(matingPool.size());

                DNA parentA = matingPool.get(a);
                DNA parentB = matingPool.get(b);

                //creating a child from the parents
                DNA child = parentA.crossover(parentB, target);

                //check to see if it will mutate
                child.mutate(mutationRate);
                //replace old generation with new generation
                population[i] = child;

            }
            //simple counter to keep track of how many generations it took to achieve the result
            numOfGenerations++;

            //simple for loop and if condition to check if any DNA object matches the target word
            for (int i = 0; i < population.length; i++) {
                if (population[i].getGenesStr().equals(target)) {
                    perfectGenome = population[i].getGenesStr();
                    notFound = false;
                    break;
                }
            }
            System.out.println("------------");

        }
        //once the loop ends it prints the first perfect DNA object contents, and the number of generations it took
        System.out.println("done");
        System.out.println("genome: "+perfectGenome);
        System.out.println("generations: "+numOfGenerations);
    }
}
