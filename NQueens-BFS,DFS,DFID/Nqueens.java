/*
 * 
 * Class name : 인공 지능

 * 
 * 이 프로그램의 목적 : 이 프로그램은 N-Queens 문제를 dfs, bfs, dfid 등의 방법으로 풀이하는 프로그램입니다.
 * 
 * 주안점 : dfs, bfs, dfid 각각의 돌아가는 방식은 다릅니다. 하지만 트리의 맨 앞쪽부터 서치하기 때문에 결과 값은 동일하게 나옵니다.
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
/*

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Nqueens {
	public static int size;
	
	
	// dfs, dfid에서는 stack을 사용
	public static Stack <ArrayList> dfid = new Stack<ArrayList>();
	public static Stack <ArrayList> dfs = new Stack<ArrayList>();
	
	// bfs에서는 queue를 사용
	public static Queue <ArrayList> bfs = new LinkedList<ArrayList>();
	
	
	/*현재 상태가 가로 세로 대각선 방향에 동시에 퀸이 존재하는지를 체크하는 함수이다.*/
	/*Array List로 Queen의 상태를 체크한다.					*/
	public static  boolean check( ArrayList < Integer > temp){
		for(int i=0;i<temp.size()-1;i++){
			for(int j=i+1;j<temp.size();j++){
				int value_1 = temp.get(i);
				int value_2 = temp.get(j);
				
				//알고리즘에서 같은 행에는 퀸이 한 개 밖에 들어갈 수 없기 때문에 대각선과 열 만을 체크한다.
				if(j-i == Math.abs(value_2-value_1)||value_1 == value_2)
					return false;
				
			}
		}
		return true;
	}
	
	static double time[] = new double[3];		//시간을 체크하는 전역변수
	
	
	//bfs 알고리즘
	public static String bfs (int N){
		System.out.println("BFS 생성 node depth check>>");
		double START_TIME = System.currentTimeMillis();		//시작시간 저장
		
		
		ArrayList<Integer> goal = new ArrayList<Integer>();	//goal을 저장할 array List생성
		String s = "Location : ";							//반환할 string을 선언과 초기화
		
		
		//bfs 큐에 depth 1인 노드들을 추가
		for(int i =0;i<N;i++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(i);
			bfs.add(temp);
		}
		
		
		//bfs 서치
		while(!bfs.isEmpty()){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			
			temp = bfs.poll(); 			//큐의 맨 앞에 있는 노드를 가져옴
			
			
			// 골을 체크하는 조건문
			if(temp.size()==N && check(temp)){
				goal = temp;
				break;
			}
			
			// 현재 상태를 체크하고 퀸이 적절히 배치되어있다면 노드들을 추가
			if(temp.size()!=N&&check(temp)){
				
				/*Search를 할 때 Pruning 하지않고 모두 Expanding*/
				for(int i =0;i<N;i++){
					ArrayList<Integer> temp_2 = new ArrayList<Integer>();
					temp_2.addAll(0, temp);
					temp_2.add(i);
					size = temp_2.size();
					bfs.add(temp_2);
				}
				System.out.print(size);
			}
		}
		
		for(int i=0;i<N;i++){
				s+=goal.indexOf(i)+" ";				// s에 출력 저장
		}
		
		double END_TIME = System.currentTimeMillis();
		time[1] = ( END_TIME - START_TIME )/1000;	// 소요시간 저장
		System.out.println("");
		return s;
	}
	
	//dfs 알고리즘
	public static String dfs ( int N ){
		System.out.println("DFS 생성 node depth check>>");
		double START_TIME = System.currentTimeMillis();		//시작시간 저장
		
		
		ArrayList<Integer> goal = new ArrayList<Integer>();	//goal을 저장할 array List생성
		String s = "Location : " ;				 			//반환할 string을 선언과 초기화
		
		
		//dfs 스택에 depth 1인 노드들을 추가
		for ( int i = N-1 ; i >= 0 ; i-- ){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add( i );
			dfs.push( temp );
		}
		
		
		//dfs 서치
		while ( !dfs.isEmpty() ){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			
			temp = dfs.pop(); 		//dfs의 top을 temp에 저장

			// 골을 체크하는 조건문
			if ( temp.size() == N && check( temp ) ){
				goal = temp;
				break;
			}
			
			
			// 현재 상태를 체크하고 퀸이 적절히 배치되어있다면 노드들을 추가한다.
			if ( temp.size() != N && check( temp )){
				
				/*Search를 할 때 Pruning 하지않고 모두 Expanding*/
				for ( int i = N-1 ; i >= 0 ; i-- ){
					ArrayList<Integer> temp_2 = new ArrayList<Integer>();
					temp_2.addAll ( 0 , temp );
					temp_2.add( i );
					size = temp_2.size();
					dfs.push( temp_2 );
					System.out.print(size);
				}
				
			}
		}
		
	
		for ( int i = 0 ; i < N ; i++){
				s += goal.indexOf( i ) + " ";			// s에 출력 저장
		}
		
		
		double END_TIME = System.currentTimeMillis();
		time[0] = ( END_TIME - START_TIME ) / 1000;		// 소요 시간 저장
		System.out.println("");
		return s;
		
	}
	
	

	//dfid 알고리즘
	public static String dfid ( int N ){
		System.out.println("DFID 생성 node depth check>>");
		double START_TIME = System.currentTimeMillis();			//시작시간 저장
		int depth_limit=1;										//depth limit을 지정할 변수를 선언및 초기화
		
		
		ArrayList<Integer> goal = new ArrayList<Integer>();		//goal을 저장할 array List생성
		String s = "Location : " ; 								//반환할 string을 선언과 초기화
		
		
		//depth_limit을 1부터 차례로 증가시키며 반복문을 진행
		for ( depth_limit = 1 ; goal.isEmpty() ; depth_limit++ ){
			dfid.clear();
			//dfid 스택에 depth 1인 노드들을 추가
			for ( int i = N-1 ; i >= 0 ; i-- ){
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add( i );
				dfid.push( temp );
			}
		
			
			//dls 서치
			while ( !dfid.isEmpty() ){
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp = dfid.pop();

				//depth_limit 체크
				if ( temp.size() != depth_limit ){
					// 골을 체크하는 조건문
					if ( temp.size() == N && check(temp) ){
						goal = temp;
						break;
					}
					
					
					// 현재 상태를 체크하고 퀸이 적절히 배치되어있다면 노드들을 추가한다.
					if(temp.size()!=N&&check(temp)){
						
						/*Search를 할 때 Pruning 하지않고 모두 Expanding*/
						for(int i =N-1;i>=0;i--){
							ArrayList<Integer> temp_2 = new ArrayList<Integer>();
							temp_2.addAll(0, temp);
							temp_2.add(i);
							size = temp_2.size();
							//System.out.print(temp_2.size());
							dfid.push(temp_2);
							
						}
						System.out.print(size);
					}
				}
			}
			
			if ( !dfid.isEmpty() ){
				for ( int i = 0 ; i < N ; i++ ){
					s += goal.indexOf( i ) + " " ;		// s에 출력 저장
				}
			}
		}
		
		
		double END_TIME = System.currentTimeMillis();
		time[2] = ( END_TIME - START_TIME ) / 1000;		// 소요 시간 저장
		System.out.println("");
		return s;
	}
	
	
	
	public static void main( String[] args )
	{
		
		int N = Integer.parseInt( args[0] );	//N은 퀸의 갯수
		
		File file1 = new File( args[1] , "result" + args[0] + ".txt" );		//File open
		
		
		/* 파일 출력 Stream 생성 */
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter( new FileOutputStream( file1 ) );
		} catch ( FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* Nqueens Problem의 해결이 가능 할때 */
		/* dfs, bfs, dfid 알고리즘 모두 불가능 한 경우를 찾아낼 수 있지만 출력을 맞추기 위해서 미리 제한했습니다.*/
		if ( N >= 4 ){
			outputStream.println( ">DFS" );
			outputStream.println( dfs( N ) );
			outputStream.println( "Time : " + time[0] );
			outputStream.println( "" );
			
			outputStream.println( ">BFS" );
			outputStream.println( bfs(N) );
			outputStream.println( "Time : " + time[1] );
			outputStream.println( "" );
			
			outputStream.println( ">DFID" );
			outputStream.println( dfid( N ) );
			outputStream.println( "Time : " + time[2] );
			outputStream.println( "" );
		}
		else{
			/* Nqueens Problem의 해결이 불가능할 때*/
			outputStream.println( ">DFS" );
			outputStream.println( "No solution" );
			outputStream.println( "Time : " + 0.0 );
			outputStream.println( "" );
			
			outputStream.println( ">BFS" );
			outputStream.println( "No solution" );
			outputStream.println( "Time : " + 0.0);
			outputStream.println( "" );
			
			outputStream.println( ">DFID" );
			outputStream.println( "No solution" );
			outputStream.println( "Time : " + 0.0);
			outputStream.println( "" );
		}
			
		
		outputStream.close();
	}
		
	
}
*/