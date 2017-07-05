/*
 * 
 * Class name : �ΰ� ����
 * 
 * �� ���α׷��� ���� : �� ���α׷��� N-Queens ������ local Search�� Hill Climbing ������� Ǯ���ϴ� ���α׷��Դϴ�.
 * 
 * �־��� : heuristic �Լ��� ���� ��� ���¿��� ���� �󸶳� ���� �浹�� �ϴ°��� �����ؼ� ���� ���� �浹 ���� ���ϰ� �߽��ϴ�.
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


public class assignment2 {
	
	static int N;						// N���� Queen
	static double elapsed_time = 0;		// ��� �ð�

	public static void main(String[] args){
		
		
		N = Integer.parseInt( args[0] );							// argument�� N�� ���� �Է�
		ArrayList < Integer > goal = new ArrayList < Integer >();	// goal�� ������ Array List ����
		
		// Queen�� 3 �̻��� ��� local Search
		if( N > 3 )
			goal = localSearch();
		
		
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
		
		
		// ��� ���� ���߱�
		outputStream.println( ">Hill Climbing" );
		if( N >= 4){
			for( int i = 0 ; i < N ; i++ )									// goal�� �ִ� ���� ���� ���� ��ġ�� ���
				outputStream.print( goal.get( i ) - 1 + " " );				// ���� �ѹ��� 1~N���� ��� �ؼ� ��� ������ ���߱� ���� -1�� ��.
			outputStream.println();
			outputStream.println( "Total Elapsed Time: " + elapsed_time );	// ��� �ð� ���
		}
		else {																// �ش��� ã�� �� ���� ���
			outputStream.println( "No Solution" );
			outputStream.println( "Total Elapsed Time: 0.000" );
		}
		outputStream.close();												// outputStream ����
	}

	
	// Goal ���� ���¸� üũ�ϴ� �Լ�
	// Goal �̸� true �ƴϸ� false�� �����Ѵ�.
	public static boolean goalcheck( ArrayList < Integer > goal){
		for( int i = 0 ; i < N ; i++)
			for( int j = i + 1 ; j < N ; j++){
				int distance = Math.abs(goal.get( i ) - goal.get( j ));		// �밢���� �浹�� Ȯ���ϱ� ���� ���� �Ÿ�
				if( goal.get( i ) == goal.get( j ) || j - i == distance)	// ���� ��ġ �Ǵ� ���� �Ÿ��� ���� �Ÿ��� ������ �浹
					return false;
				
			}
		return true;
	}
	
	
	/*		heuristic ���� future cost�� ���ϴ� �Լ� 			*/
	/*		 �浹�� ������ heuristic ������ ���ߴ�. 				*/
	public static int heuristic( ArrayList < Integer > heuris){
		int collision = 0 ;			//�浹�� ������ �޴� ����
		
		
		/* ���� �࿡�� ���� �浹�ϴ� Ƚ���� ����. */
		for( int i = 0 ; i < N ; i++ )
			for( int j = i + 1 ; j < N ; j++)
				if( heuris.get( i ) == heuris.get( j ) )
					collision++;
		
		/* �밢������ ���� �浹�ϴ� Ƚ���� ����. */
		for( int i = 0 ; i<N; i++)
			for( int j = i+1; j<N;j++){
				if(j-i == Math.abs(heuris.get(j)-heuris.get(i)))
						collision++;
			}
		
		return collision;		// �� �浹 Ƚ��(heuristic��)�� ��ȯ�Ѵ�.
	}
	
	
	/* �����ϰ� ���� ��ġ�ϴ� �Լ� */
	public static ArrayList<Integer> randomQueen(){
		ArrayList < Integer > random = new ArrayList < Integer >();
		
		/* Math ��� object�� random method�� �̿��Ͽ� ���� ��ġ�� �����ϰ� ���Ѵ�.*/
		for( int i = 0 ; i < N ; i++){
			double randomnumber = Math.random() * N + 1 ; 	// 1~N ������ ��ġ�� ���Ƿ� ���ϴ� ����
			random.add( ( int )randomnumber );
		}
		
		return random ;
	}
	
	
	
	/* local search �Լ� */
	public static ArrayList < Integer > localSearch(){
		elapsed_time = System.currentTimeMillis(); 		// Search�� ���� �ð��� ����
		int localopticheck = 0;							// local optimal ������ üũ�ϴ� ����
		
		/* N-queen Node ���� �� ���� �ʱ�ȭ */
		ArrayList < Integer > globalopti = new ArrayList < Integer >();
		globalopti = randomQueen();
		
		
		while( true ){
			localopticheck++ ;	// local optimal���� ��ü�ǰ� �ִ��� Ȯ��
			
			/* ��� �� �ϳ� �� ������ �� �ִ� ��쿡�� heuristic �Լ��� �ּ� ���� ���ϰ� �̿� �ش��ϴ� ��带 globalopti�� ���� */
			for( int i = 0 ; i < N ; i++ ){
				for( int j = 1 ; j < N + 1 ; j++ ){
					ArrayList < Integer > temp = new ArrayList < Integer >();
					temp.addAll( globalopti );
					temp.set( i, j );
					if( heuristic( globalopti ) > heuristic( temp ) )		// ���� ����� �޸���ƽ ���� �̵����� ���� �� �� ���� ���� ����
						globalopti = temp ;
				}
			}
			
			/* local optimal �� ��� ����� */
			if( localopticheck > 30 ){
				localopticheck = 0 ;
				globalopti = randomQueen() ;
			}
			else if( goalcheck ( globalopti ) ){ 	// goal check�� �ؼ� ����� �ش� ��� ��ȯ
				// elapsed_time�� �Լ� ���۽� ����Ǿ��ִ� ���� �ð��� ��ȯ �� �ð����� �� �� �ʴ����� ���߱� ���� 1000���� ������.
				elapsed_time = ( System.currentTimeMillis() - elapsed_time ) / 1000 ;
				return globalopti ;
				
			}
		}
		
		
	}
	
	
	
	
	
	
}
