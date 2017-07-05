/*
 * 
 * Class name : 인공 지능
 * 
 * 이 프로그램의 목적 : 이 프로그램은 N-Queens 문제를 local Search의 Hill Climbing 방법으로 풀이하는 프로그램입니다.
 * 
 * 주안점 : heuristic 함수를 현재 노드 상태에서 퀸이 얼마나 많은 충돌을 하는가를 측정해서 가장 적은 충돌 수로 향하게 했습니다.
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


public class assignment2 {
	
	static int N;						// N개의 Queen
	static double elapsed_time = 0;		// 경과 시간

	public static void main(String[] args){
		
		
		N = Integer.parseInt( args[0] );							// argument로 N의 값을 입력
		ArrayList < Integer > goal = new ArrayList < Integer >();	// goal을 저장할 Array List 선언
		
		// Queen이 3 이상일 경우 local Search
		if( N > 3 )
			goal = localSearch();
		
		
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
		
		
		// 출력 형식 맞추기
		outputStream.println( ">Hill Climbing" );
		if( N >= 4){
			for( int i = 0 ; i < N ; i++ )									// goal에 있는 퀸의 열당 행의 위치를 출력
				outputStream.print( goal.get( i ) - 1 + " " );				// 행의 넘버를 1~N으로 계산 해서 출력 형식을 맞추기 위해 -1을 함.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time );	// 경과 시간 출력
		}
		else {																// 해답을 찾을 수 없을 경우
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		outputStream.close();												// outputStream 종료
	}

	
	// Goal 인지 상태를 체크하는 함수
	// Goal 이면 true 아니면 false를 리턴한다.
	public static boolean goalcheck( ArrayList < Integer > goal){
		for( int i = 0 ; i < N ; i++)
			for( int j = i + 1 ; j < N ; j++){
				int distance = Math.abs(goal.get( i ) - goal.get( j ));		// 대각선의 충돌을 확인하기 위한 열의 거리
				if( goal.get( i ) == goal.get( j ) || j - i == distance)	// 열의 위치 또는 행의 거리와 열의 거리가 같으면 충돌
					return false;
				
			}
		return true;
	}
	
	
	/*		heuristic 값인 future cost를 구하는 함수 			*/
	/*		 충돌의 갯수를 heuristic 값으로 정했다. 				*/
	public static int heuristic( ArrayList < Integer > heuris){
		int collision = 0 ;			//충돌의 갯수를 받는 변수
		
		
		/* 같은 행에서 퀸이 충돌하는 횟수를 센다. */
		for( int i = 0 ; i < N ; i++ )
			for( int j = i + 1 ; j < N ; j++)
				if( heuris.get( i ) == heuris.get( j ) )
					collision++;
		
		/* 대각선으로 퀸이 충돌하는 횟수를 센다. */
		for( int i = 0 ; i<N; i++)
			for( int j = i+1; j<N;j++){
				if(j-i == Math.abs(heuris.get(j)-heuris.get(i)))
						collision++;
			}
		
		return collision;		// 총 충돌 횟수(heuristic값)을 반환한다.
	}
	
	
	/* 랜덤하게 퀸을 배치하는 함수 */
	public static ArrayList<Integer> randomQueen(){
		ArrayList < Integer > random = new ArrayList < Integer >();
		
		/* Math 라는 object의 random method를 이용하여 퀸의 위치를 랜덤하게 정한다.*/
		for( int i = 0 ; i < N ; i++){
			double randomnumber = Math.random() * N + 1 ; 	// 1~N 까지의 위치를 임의로 정하는 수식
			random.add( ( int )randomnumber );
		}
		
		return random ;
	}
	
	
	
	/* local search 함수 */
	public static ArrayList < Integer > localSearch(){
		elapsed_time = System.currentTimeMillis(); 		// Search의 시작 시간을 저장
		int localopticheck = 0;							// local optimal 인지를 체크하는 숫자
		
		/* N-queen Node 선언 및 랜덤 초기화 */
		ArrayList < Integer > globalopti = new ArrayList < Integer >();
		globalopti = randomQueen();
		
		
		while( true ){
			localopticheck++ ;	// local optimal에서 정체되고 있는지 확인
			
			/* 모든 퀸 하나 당 움직일 수 있는 경우에서 heuristic 함수의 최소 값을 구하고 이에 해당하는 노드를 globalopti에 저장 */
			for( int i = 0 ; i < N ; i++ ){
				for( int j = 1 ; j < N + 1 ; j++ ){
					ArrayList < Integer > temp = new ArrayList < Integer >();
					temp.addAll( globalopti );
					temp.set( i, j );
					if( heuristic( globalopti ) > heuristic( temp ) )		// 현재 저장된 휴리스틱 값과 이동했을 때의 값 중 작은 값을 저장
						globalopti = temp ;
				}
			}
			
			/* local optimal 일 경우 재시작 */
			if( localopticheck > 30 ){
				localopticheck = 0 ;
				globalopti = randomQueen() ;
			}
			else if( goalcheck ( globalopti ) ){ 	// goal check를 해서 통과시 해당 노드 반환
				// elapsed_time에 함수 시작시 저장되어있던 시작 시간을 반환 전 시간에서 뺀 후 초단위로 맞추기 위해 1000으로 나눴다.
				elapsed_time = ( System.currentTimeMillis() - elapsed_time ) / 1000 ;
				return globalopti ;
				
			}
		}
		
		
	}
	
	
	
	
	
	
}
