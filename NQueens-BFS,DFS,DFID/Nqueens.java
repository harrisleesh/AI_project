/*
 * 
 * Class name : �ΰ� ����

 * 
 * �� ���α׷��� ���� : �� ���α׷��� N-Queens ������ dfs, bfs, dfid ���� ������� Ǯ���ϴ� ���α׷��Դϴ�.
 * 
 * �־��� : dfs, bfs, dfid ������ ���ư��� ����� �ٸ��ϴ�. ������ Ʈ���� �� ���ʺ��� ��ġ�ϱ� ������ ��� ���� �����ϰ� ���ɴϴ�.
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
	
	
	// dfs, dfid������ stack�� ���
	public static Stack <ArrayList> dfid = new Stack<ArrayList>();
	public static Stack <ArrayList> dfs = new Stack<ArrayList>();
	
	// bfs������ queue�� ���
	public static Queue <ArrayList> bfs = new LinkedList<ArrayList>();
	
	
	/*���� ���°� ���� ���� �밢�� ���⿡ ���ÿ� ���� �����ϴ����� üũ�ϴ� �Լ��̴�.*/
	/*Array List�� Queen�� ���¸� üũ�Ѵ�.					*/
	public static  boolean check( ArrayList < Integer > temp){
		for(int i=0;i<temp.size()-1;i++){
			for(int j=i+1;j<temp.size();j++){
				int value_1 = temp.get(i);
				int value_2 = temp.get(j);
				
				//�˰��򿡼� ���� �࿡�� ���� �� �� �ۿ� �� �� ���� ������ �밢���� �� ���� üũ�Ѵ�.
				if(j-i == Math.abs(value_2-value_1)||value_1 == value_2)
					return false;
				
			}
		}
		return true;
	}
	
	static double time[] = new double[3];		//�ð��� üũ�ϴ� ��������
	
	
	//bfs �˰���
	public static String bfs (int N){
		System.out.println("BFS ���� node depth check>>");
		double START_TIME = System.currentTimeMillis();		//���۽ð� ����
		
		
		ArrayList<Integer> goal = new ArrayList<Integer>();	//goal�� ������ array List����
		String s = "Location : ";							//��ȯ�� string�� ����� �ʱ�ȭ
		
		
		//bfs ť�� depth 1�� ������ �߰�
		for(int i =0;i<N;i++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(i);
			bfs.add(temp);
		}
		
		
		//bfs ��ġ
		while(!bfs.isEmpty()){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			
			temp = bfs.poll(); 			//ť�� �� �տ� �ִ� ��带 ������
			
			
			// ���� üũ�ϴ� ���ǹ�
			if(temp.size()==N && check(temp)){
				goal = temp;
				break;
			}
			
			// ���� ���¸� üũ�ϰ� ���� ������ ��ġ�Ǿ��ִٸ� ������ �߰�
			if(temp.size()!=N&&check(temp)){
				
				/*Search�� �� �� Pruning �����ʰ� ��� Expanding*/
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
				s+=goal.indexOf(i)+" ";				// s�� ��� ����
		}
		
		double END_TIME = System.currentTimeMillis();
		time[1] = ( END_TIME - START_TIME )/1000;	// �ҿ�ð� ����
		System.out.println("");
		return s;
	}
	
	//dfs �˰���
	public static String dfs ( int N ){
		System.out.println("DFS ���� node depth check>>");
		double START_TIME = System.currentTimeMillis();		//���۽ð� ����
		
		
		ArrayList<Integer> goal = new ArrayList<Integer>();	//goal�� ������ array List����
		String s = "Location : " ;				 			//��ȯ�� string�� ����� �ʱ�ȭ
		
		
		//dfs ���ÿ� depth 1�� ������ �߰�
		for ( int i = N-1 ; i >= 0 ; i-- ){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add( i );
			dfs.push( temp );
		}
		
		
		//dfs ��ġ
		while ( !dfs.isEmpty() ){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			
			temp = dfs.pop(); 		//dfs�� top�� temp�� ����

			// ���� üũ�ϴ� ���ǹ�
			if ( temp.size() == N && check( temp ) ){
				goal = temp;
				break;
			}
			
			
			// ���� ���¸� üũ�ϰ� ���� ������ ��ġ�Ǿ��ִٸ� ������ �߰��Ѵ�.
			if ( temp.size() != N && check( temp )){
				
				/*Search�� �� �� Pruning �����ʰ� ��� Expanding*/
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
				s += goal.indexOf( i ) + " ";			// s�� ��� ����
		}
		
		
		double END_TIME = System.currentTimeMillis();
		time[0] = ( END_TIME - START_TIME ) / 1000;		// �ҿ� �ð� ����
		System.out.println("");
		return s;
		
	}
	
	

	//dfid �˰���
	public static String dfid ( int N ){
		System.out.println("DFID ���� node depth check>>");
		double START_TIME = System.currentTimeMillis();			//���۽ð� ����
		int depth_limit=1;										//depth limit�� ������ ������ ����� �ʱ�ȭ
		
		
		ArrayList<Integer> goal = new ArrayList<Integer>();		//goal�� ������ array List����
		String s = "Location : " ; 								//��ȯ�� string�� ����� �ʱ�ȭ
		
		
		//depth_limit�� 1���� ���ʷ� ������Ű�� �ݺ����� ����
		for ( depth_limit = 1 ; goal.isEmpty() ; depth_limit++ ){
			dfid.clear();
			//dfid ���ÿ� depth 1�� ������ �߰�
			for ( int i = N-1 ; i >= 0 ; i-- ){
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add( i );
				dfid.push( temp );
			}
		
			
			//dls ��ġ
			while ( !dfid.isEmpty() ){
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp = dfid.pop();

				//depth_limit üũ
				if ( temp.size() != depth_limit ){
					// ���� üũ�ϴ� ���ǹ�
					if ( temp.size() == N && check(temp) ){
						goal = temp;
						break;
					}
					
					
					// ���� ���¸� üũ�ϰ� ���� ������ ��ġ�Ǿ��ִٸ� ������ �߰��Ѵ�.
					if(temp.size()!=N&&check(temp)){
						
						/*Search�� �� �� Pruning �����ʰ� ��� Expanding*/
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
					s += goal.indexOf( i ) + " " ;		// s�� ��� ����
				}
			}
		}
		
		
		double END_TIME = System.currentTimeMillis();
		time[2] = ( END_TIME - START_TIME ) / 1000;		// �ҿ� �ð� ����
		System.out.println("");
		return s;
	}
	
	
	
	public static void main( String[] args )
	{
		
		int N = Integer.parseInt( args[0] );	//N�� ���� ����
		
		File file1 = new File( args[1] , "result" + args[0] + ".txt" );		//File open
		
		
		/* ���� ��� Stream ���� */
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter( new FileOutputStream( file1 ) );
		} catch ( FileNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* Nqueens Problem�� �ذ��� ���� �Ҷ� */
		/* dfs, bfs, dfid �˰��� ��� �Ұ��� �� ��츦 ã�Ƴ� �� ������ ����� ���߱� ���ؼ� �̸� �����߽��ϴ�.*/
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
			/* Nqueens Problem�� �ذ��� �Ұ����� ��*/
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