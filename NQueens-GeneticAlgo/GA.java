/*
 * 
 * Class name : 인공 지능
 * 
 * 이 프로그램의 목적 : 이 프로그램은 N-Queens 문제를 Genetic Algorithm 방법으로 풀이하는 프로그램입니다.
 * 
 * 주안점 : parent 선택과, crossover, mutation등의 주요 함수들의 방식
 * 
 * fitness 함수를 현재 노드 상태에서 퀸이 얼마나 많은 충돌을 하는가를 측정해서 가장 적은 충돌 수가 가장 적합하다고 했습니다.
 * 
 * 시간의 단위는 sec 입니다.
 * 
 * 출력은 각 0 ~ N-1 까지의 Column에 있는 각 Queen의 row의 위치를 출력합니다.
 * 
 * 문제를 풀 수 없는경우 No solution을 출력하고 시간은 0.0을 출력합니다.
 * 
 * 최대한 code conventions에 맞춰서 프로그래밍 하고자 노력했습니다.
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
	static int N;						// N개의 Queen
	static double elapsed_time = 0;		// 경과 시간
	static int population=100;			//population number
	
	public static void main(String args[]){
		
		//시작 시간을 측정
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
		
		//Queen이 4 이상일 경우 Genetic algorithm을 실행
		if(N>4){
			//골이 비어있으면 무한 반복
			while(Goal.isEmpty()){
				List new_gen = new ArrayList();
				
				//parent selection 함수
				new_gen.addAll(parentselection(generation, psel_num, population/5));
				
				//crossover operation
				new_gen.addAll(crossover(generation,csover_num));
				
				//mutation operation
				new_gen.addAll(mutation(generation,mute_num));
				generation.clear();
				generation.addAll(new_gen);
				new_gen.clear();
				
				//fitness를 각 원소마다 체크한다.
				for(int i = 0; i<population; i++){
					if(fitness((ArrayList)generation.get(i))==0){
						//fitness is 0, means no collision, it means goal
						Goal=(ArrayList)generation.get(i);
						
					}
				}
			}
		}
		
		
		// 파일 이름 형식
		String filename = new String();
		filename = "result";
		filename += N;
		filename += ".txt";
		
		
		// 파일 출력 class를 이용
		File output = new File( args[1] , filename );
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileOutputStream(output));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// elapsed_time에 함수 시작시 저장되어있던 시작 시간을 반환 전 시간에서 뺀 후 초단위로 맞추기 위해 1000으로 나눴다.
		elapsed_time = ( System.currentTimeMillis() - elapsed_time ) / 1000 ;
		
		
		// 출력 형식 맞추기
		outputStream.println( ">Genetic Algorithm" );
		if( N >= 4){
			for( int i = 0 ; i < N ; i++ )									// goal에 있는 퀸의 열당 행의 위치를 출력
				outputStream.print( Goal.get( i )  + " " );					// 행의 넘버를 1~N으로 계산 해서 출력 형식을 맞추기 위해 -1을 함.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time );	// 경과 시간 출력
		}
		else {																// 해답을 찾을 수 없을 경우
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		outputStream.close();												// outputStream 종료

		
	}
	
	
	
	
	/*		 충돌의 갯수를 fitness 값으로 정했다. 				*/
	public static int fitness ( ArrayList < Integer > fit){
		int collision = 0 ;			//충돌의 갯수를 받는 변수
		
		
		/* 같은 행에서 퀸이 충돌하는 횟수를 센다. */
		for( int i = 0 ; i < N ; i++ )
			for( int j = i + 1 ; j < N ; j++)
				if( fit.get( i ) == fit.get( j ) )
					collision++;
		
		/* 대각선으로 퀸이 충돌하는 횟수를 센다. */
		for( int i = 0 ; i<N; i++)
			for( int j = i+1; j<N;j++){
				if(j-i == Math.abs(fit.get(j)-fit.get(i)))
						collision++;
			}
		
		return collision;		// 총 충돌 횟수(fitness값)을 반환한다.
	}
	
	
	/* 랜덤하게 퀸을 배치하는 함수 */
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
			int fitness=population;								//fitness값은 적을수록 좋다.
			ArrayList<Integer> temp;
			
			//토너먼트 셀렉션 방식으로 parent를 지정한다.
			for(int j =0; j< Num; j++){
				
				temp = (ArrayList)generation.get(index.nextInt(population));
				int collis=fitness(temp);
				
				//충돌이 가장 적은 부모를 선택적으로 취합
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
			//두개의 parents를 랜덤하게 선택한다.
			ArrayList<Integer> selectedParent_1;
			ArrayList<Integer> selectedParent_2;
			selectedParent_1 = (ArrayList)generation.get(index.nextInt(population));
			selectedParent_2 = (ArrayList)generation.get(index.nextInt(population));
			
			//crossover할 포인트를 랜덤하게 선택한다.
			int randomPoint = index.nextInt(N);
			
			//랜덤 포인트를 지정해서 크로스 오버한다.
			for (int j = 0; j<randomPoint; j++){
				int temp;
				temp= selectedParent_1.get(j);
				selectedParent_1.remove(j);
				selectedParent_1.add(j,selectedParent_2.get(j));
				selectedParent_2.remove(j);
				selectedParent_2.add(j, temp);
			}
			
			//두 parents모두 complex_2th에 넣어준다.	
			complex_2th.add(selectedParent_2);
			complex_2th.add(selectedParent_1);
			
			
		}
		
		return complex_2th;
	}
	
	// mutation을 하는 함수
	public static List mutation(List generation, int Num){
		List mutant_2th = new ArrayList();
		Random index = new Random();
		
		for(int i=0; i<Num; i++){
			ArrayList<Integer> temp = new ArrayList();
			temp = (ArrayList)generation.get(index.nextInt(population));
			
			//mutation할 point를 랜덤하게 지정해주고 지정 포인트까지의 원소를 다 제거한다.
			int mutate_point = index.nextInt(N);
			for (int j=0; j < mutate_point ; j++){
				temp.remove(0);
			}
			
			//랜덤한 수를 넣되 기존에 있는 수라면 넣지않는다.
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
