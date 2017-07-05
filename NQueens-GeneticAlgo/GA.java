/*
 * 
 * Class name : �ΰ� ����
 * 
 * �� ���α׷��� ���� : �� ���α׷��� N-Queens ������ Genetic Algorithm ������� Ǯ���ϴ� ���α׷��Դϴ�.
 * 
 * �־��� : parent ���ð�, crossover, mutation���� �ֿ� �Լ����� ���
 * 
 * fitness �Լ��� ���� ��� ���¿��� ���� �󸶳� ���� �浹�� �ϴ°��� �����ؼ� ���� ���� �浹 ���� ���� �����ϴٰ� �߽��ϴ�.
 * 
 * �ð��� ������ sec �Դϴ�.
 * 
 * ����� �� 0 ~ N-1 ������ Column�� �ִ� �� Queen�� row�� ��ġ�� ����մϴ�.
 * 
 * ������ Ǯ �� ���°�� No solution�� ����ϰ� �ð��� 0.0�� ����մϴ�.
 * 
 * �ִ��� code conventions�� ���缭 ���α׷��� �ϰ��� ����߽��ϴ�.
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GA {
	static int N;						// N���� Queen
	static double elapsed_time = 0;		// ��� �ð�
	static int population=100;			//population number
	
	public static void main(String args[]){
		
		//���� �ð��� ����
		elapsed_time = System.currentTimeMillis();
		
		N = Integer.parseInt(args[0]);
		
		double psel_rate=0.4;											//parent selection rate
		double csover_rate=0.3;											//crossover rate
		double mute_rate=0.3;											//mutation rate
		
		int psel_num= (int)(population * psel_rate);					//parent selection number
		int csover_num=(int)(population *csover_rate);					//crossover number
		int mute_num=(int)(population *mute_rate);						//mutation number
		
		List generation = new ArrayList();
		
		ArrayList <Integer> Goal = new ArrayList();
		
		for(int i = 0; i<population; i++){
			ArrayList<Integer> child = new ArrayList();
			child = randomQueen();
			generation.add(child);
			
		}
		
		//Queen�� 4 �̻��� ��� Genetic algorithm�� ����
		if(N>4){
			//���� ��������� ���� �ݺ�
			while(Goal.isEmpty()){
				List new_gen = new ArrayList();
				
				//parent selection �Լ�
				new_gen.addAll(parentselection(generation, psel_num, population/5));
				
				//crossover operation
				new_gen.addAll(crossover(generation,csover_num));
				
				//mutation operation
				new_gen.addAll(mutation(generation,mute_num));
				generation.clear();
				generation.addAll(new_gen);
				new_gen.clear();
				
				//fitness�� �� ���Ҹ��� üũ�Ѵ�.
				for(int i = 0; i<population; i++){
					if(fitness((ArrayList)generation.get(i))==0){
						//fitness is 0, means no collision, it means goal
						Goal=(ArrayList)generation.get(i);
						
					}
				}
			}
		}
		
		
		// ���� �̸� ����
		String filename = new String();
		filename = "result";
		filename += N;
		filename += ".txt";
		
		
		// ���� ��� class�� �̿�
		File output = new File( args[1] , filename );
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileOutputStream(output));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// elapsed_time�� �Լ� ���۽� ����Ǿ��ִ� ���� �ð��� ��ȯ �� �ð����� �� �� �ʴ����� ���߱� ���� 1000���� ������.
		elapsed_time = ( System.currentTimeMillis() - elapsed_time ) / 1000 ;
		
		
		// ��� ���� ���߱�
		outputStream.println( ">Genetic Algorithm" );
		if( N >= 4){
			for( int i = 0 ; i < N ; i++ )									// goal�� �ִ� ���� ���� ���� ��ġ�� ���
				outputStream.print( Goal.get( i )  + " " );					// ���� �ѹ��� 1~N���� ��� �ؼ� ��� ������ ���߱� ���� -1�� ��.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time );	// ��� �ð� ���
		}
		else {																// �ش��� ã�� �� ���� ���
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		outputStream.close();												// outputStream ����

		
	}
	
	
	
	
	/*		 �浹�� ������ fitness ������ ���ߴ�. 				*/
	public static int fitness ( ArrayList < Integer > fit){
		int collision = 0 ;			//�浹�� ������ �޴� ����
		
		
		/* ���� �࿡�� ���� �浹�ϴ� Ƚ���� ����. */
		for( int i = 0 ; i < N ; i++ )
			for( int j = i + 1 ; j < N ; j++)
				if( fit.get( i ) == fit.get( j ) )
					collision++;
		
		/* �밢������ ���� �浹�ϴ� Ƚ���� ����. */
		for( int i = 0 ; i<N; i++)
			for( int j = i+1; j<N;j++){
				if(j-i == Math.abs(fit.get(j)-fit.get(i)))
						collision++;
			}
		
		return collision;		// �� �浹 Ƚ��(fitness��)�� ��ȯ�Ѵ�.
	}
	
	
	/* �����ϰ� ���� ��ġ�ϴ� �Լ� */
	public static ArrayList<Integer> randomQueen(){
		ArrayList < Integer > random = new ArrayList < Integer >();
		Random index = new Random();
		
		while(random.size()!=N){
			int temp;
			temp = index.nextInt(N);
			if(random.indexOf(temp)==-1)
				random.add(temp);
		}
		return random ;
	}
	
	//tournament parent select by parameter number 
	//K times select from Num
	public static List parentselection(List generation,int K, int Num){
		List parent_2th = new ArrayList();
		Random index = new Random();
		for (int i=0;i<K;i++){
			ArrayList<Integer> selectedParents = new ArrayList();
			int fitness=population;								//fitness���� �������� ����.
			ArrayList<Integer> temp;
			
			//��ʸ�Ʈ ������ ������� parent�� �����Ѵ�.
			for(int j =0; j< Num; j++){
				
				temp = (ArrayList)generation.get(index.nextInt(population));
				int collis=fitness(temp);
				
				//�浹�� ���� ���� �θ� ���������� ����
				if(fitness > collis){
					selectedParents.clear();
					selectedParents.addAll(temp);
					fitness = collis;
				}
					
			}
			
			parent_2th.add(selectedParents);
			fitness = population;
		}
		return parent_2th;
	}
	
	
	//crossover as many as Num parameter in random point
	public static List crossover(List generation,int Num){
		List complex_2th = new ArrayList();
		Random index = new Random();
		for(int i = 0; i < Num/2; i++){
			//�ΰ��� parents�� �����ϰ� �����Ѵ�.
			ArrayList<Integer> selectedParent_1;
			ArrayList<Integer> selectedParent_2;
			selectedParent_1 = (ArrayList)generation.get(index.nextInt(population));
			selectedParent_2 = (ArrayList)generation.get(index.nextInt(population));
			
			//crossover�� ����Ʈ�� �����ϰ� �����Ѵ�.
			int randomPoint = index.nextInt(N);
			
			//���� ����Ʈ�� �����ؼ� ũ�ν� �����Ѵ�.
			for (int j = 0; j<randomPoint; j++){
				int temp;
				temp= selectedParent_1.get(j);
				selectedParent_1.remove(j);
				selectedParent_1.add(j,selectedParent_2.get(j));
				selectedParent_2.remove(j);
				selectedParent_2.add(j, temp);
			}
			
			//�� parents��� complex_2th�� �־��ش�.	
			complex_2th.add(selectedParent_2);
			complex_2th.add(selectedParent_1);
			
			
		}
		
		return complex_2th;
	}
	
	// mutation�� �ϴ� �Լ�
	public static List mutation(List generation, int Num){
		List mutant_2th = new ArrayList();
		Random index = new Random();
		
		for(int i=0; i<Num; i++){
			ArrayList<Integer> temp = new ArrayList();
			temp = (ArrayList)generation.get(index.nextInt(population));
			
			//mutation�� point�� �����ϰ� �������ְ� ���� ����Ʈ������ ���Ҹ� �� �����Ѵ�.
			int mutate_point = index.nextInt(N);
			for (int j=0; j < mutate_point ; j++){
				temp.remove(0);
			}
			
			//������ ���� �ֵ� ������ �ִ� ����� �����ʴ´�.
			while(temp.size()!=N){
				int rand = index.nextInt(N);
				if(temp.indexOf(rand)==-1)
					temp.add(0, rand);
			}
			mutant_2th.add(temp);
		}
		
		return mutant_2th;
		
	}
}
