package com.yc.vh.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {
	private static final String DRIVERCLASSNAME = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/vocabularyhelper?useOldAliasMetadataBehavior=true&useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8";
	private static final String USER = "root";
	private static final String PASSEORD = "12345";
	
	//��̬�죬������������
	static{
		try {
			Class.forName(DRIVERCLASSNAME);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
			
		}
	}

	/**
	 * ��ȡ����
	 */
	public  Connection getConnection(){
		Connection con = null;
		try {
			con=DriverManager.getConnection(URL, USER, PASSEORD);
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}
		return con;
	}

	/**
	 * ���ݿ����ӳصķ�ʽ��ȡ����
	 */
	//	public Connection getConnection(){
	//		//javax.naming.Context�ṩ�˲���JNDI �Ľӿ�
	//		try {
	//			Context ctx=new InitialContext();
	//			DataSource ds=(DataSource) ctx.lookup("java:comp/env/jdbc/news");
	//			con=ds.getConnection(); //��JNDI��ȡ��������Դ��������
	//		} catch (NamingException e) {
	//			e.printStackTrace();
	//			
	//		} catch (SQLException e) {
	//			
	//			throw new RuntimeException(e);
	//		}
	//		return con;
	//	}

	/**
	 * �رյķ���
	 */
	private void close(Connection con, PreparedStatement pstmt, ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}

		if(pstmt != null){
			try {
				pstmt.close();
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}

		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * ����PreparedStatement�����sql����еĲ�����
	 */
	private void setParams(PreparedStatement pstmt, List<Object> params){
		if(pstmt == null || params == null || params.isEmpty()) {
			return;
		}

		for(int i = 0, len = params.size(); i < len; i++){
			try {
				pstmt.setObject(i+1, params.get(i));
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * ����PreparedStatement�����sql����еĲ�����
	 */
	private void setParams(PreparedStatement pstmt, Object ... params){
		if(pstmt == null || params == null || params.length <= 0) {
			return;
		}

		for(int i = 0, len = params.length; i < len; i++){
			try {
				pstmt.setObject(i+1, params[i]);
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * ��ɾ��
	 * @param sql��sql��伯�ϣ�������Լӣ�
	 * @param params����ʾ?��Ӧ�Ĳ���ֵ�ļ���
	 * @return int:���ص�ֵ���ɹ�>0��ʧ��<=0
	 */
	public int update(List<String> sql, List<List<Object>> params){
		Connection con = null;
		PreparedStatement pstmt = null;
		int result=0;
		try {
			con = getConnection();	
			con.setAutoCommit(false);  //������
			for(int i = 0, len = sql.size(); i < len; i++){
				pstmt = con.prepareStatement(sql.get(i));  //Ԥ�������
				this.setParams(pstmt, params.get(i));    //���ò���
				result = pstmt.executeUpdate();
			}
			con.commit(); //û�д�ִ��
		} catch (SQLException e) {
			
			try {
				con.rollback();  //����ع�
			} catch (SQLException e1) {
				
				throw new RuntimeException(e);
			}
			throw new RuntimeException(e);
		}finally{
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				
				throw new RuntimeException(e);
			}
			this.close(con, pstmt, null);
		}
		return result;
	}

	/**
	 * ������ɾ��
	 * @param sql��sql��伯�ϣ�������Լӣ�
	 * @param params����ʾ?��Ӧ�Ĳ���ֵ�ļ���
	 * @return int:���ص�ֵ���ɹ�>0��ʧ��<=0
	 */
	public int update(String sql, List<Object> params){
		Connection con = null;
		PreparedStatement pstmt = null;
		int result=0;
		try {
			con = this.getConnection();	
			pstmt = con.prepareStatement(sql);  //Ԥ�������
			this.setParams(pstmt, params);    //���ò���
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, null);
		}
		return result;
	}

	/**
	 * ������ɾ��
	 * @param sql��sql��伯�ϣ�������Լӣ�
	 * @param params����ʾ?��Ӧ�Ĳ���ֵ�ļ���
	 * @return int:���ص�ֵ���ɹ�>0��ʧ��<=0
	 */
	public int update(String sql, Object ... params){
		Connection con = null;
		PreparedStatement pstmt = null;
		int result=0;
		try {
			con = this.getConnection();	
			pstmt = con.prepareStatement(sql);  //Ԥ�������
			this.setParams(pstmt, params);    //���ò���
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, null);
		}
		return result;
	}

	/**
	 * ��ȡ������������е�����
	 * @param rs ���������
	 * @return 
	 * @throws SQLException 
	 */
	private String[] getColumnNames(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData(); // ��ȡ������е�Ԫ����
		int colCount = rsmd.getColumnCount(); // ��ȡ��������е�����
		String[] colNames = new String[colCount];
		for (int i = 1; i <= colCount; i ++) { // ѭ����ȡ��������е�����
			colNames[i - 1] = rsmd.getColumnName(i).toLowerCase(); // �������ĳ�Сд��浽������
		}
		return colNames;
	}

	/**
	 * ��ѯ
	 * @param sql Ҫִ�еĲ�ѯ���
	 * @param params Ҫִ�е�sql����ж�Ӧռλ��?��ֵ��������?��˳�������ֵ
	 * @return �������������� ÿһ�����ݴ浽һ��map��������Ϊ�����Զ�Ӧ�е�ֵλ�ã�Ȼ��ÿһ�����ݼ�map����浽list��
	 */
	public List<Map<String, String>> finds(String sql, Object ... params) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // ��ȡ����
			pstmt = con.prepareStatement(sql); // Ԥ�������
			this.setParams(pstmt, params); // ��Ԥ��������е�ռλ����ֵ
			rs = pstmt.executeQuery(); // ִ�в�ѯ
			Map<String, String> map = null;

			// �����ȡ��������е����� -> ȡ�����������Ǵ浽һ�������У����ں����ѭ��ȡֵ -> ���ȷ������Ĵ�С?
			String[] colNames = this.getColumnNames(rs);
			while(rs.next()) { // ÿ��ѭ���õ�һ������
				map = new HashMap<String, String>();

				// ѭ����ȡÿһ�е�ֵ��ѭ�����е�����������������ȡ��ǰ��һ����һ�е�ֵ
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return list;
	}

	public List<Map<String, String>> finds(String sql, List<Object> params) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // ��ȡ����
			pstmt = con.prepareStatement(sql); // Ԥ�������
			this.setParams(pstmt, params); // ��Ԥ��������е�ռλ����ֵ
			rs = pstmt.executeQuery(); // ִ�в�ѯ
			Map<String, String> map = null;

			// �����ȡ��������е����� -> ȡ�����������Ǵ浽һ�������У����ں����ѭ��ȡֵ -> ���ȷ������Ĵ�С?
			String[] colNames = this.getColumnNames(rs);
			while(rs.next()) { // ÿ��ѭ���õ�һ������
				map = new HashMap<String, String>();

				// ѭ����ȡÿһ�е�ֵ��ѭ�����е�����������������ȡ��ǰ��һ����һ�е�ֵ
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return list;
	}

	/**
	 * ��ѯ����
	 * @param sql Ҫִ�еĲ�ѯ���
	 * @param params Ҫִ�е�sql����ж�Ӧռλ��?��ֵ��������?��˳�������ֵ
	 * @return �������������� ÿһ�����ݴ浽һ��map��������Ϊ�����Զ�Ӧ�е�ֵλ�ã�Ȼ��ÿһ�����ݼ�map����浽list��
	 */
	public Map<String, String> find(String sql, Object ... params) {
		Map<String, String> map = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(); // ��ȡ����
			pstmt = con.prepareStatement(sql); // Ԥ�������
			this.setParams(pstmt, params); // ��Ԥ��������е�ռλ����ֵ
			rs = pstmt.executeQuery(); // ִ�в�ѯ

			// �����ȡ��������е����� -> ȡ�����������Ǵ浽һ�������У����ں����ѭ��ȡֵ -> ���ȷ������Ĵ�С?
			String[] colNames = this.getColumnNames(rs);
			if(rs.next()) { // ÿ��ѭ���õ�һ������
				map = new HashMap<String, String>();

				// ѭ����ȡÿһ�е�ֵ��ѭ�����е�����������������ȡ��ǰ��һ����һ�е�ֵ
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return map;
	}

	public Map<String, String> find(String sql, List<Object> params) {
		Map<String, String> map = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // ��ȡ����
			pstmt = con.prepareStatement(sql); // Ԥ�������
			this.setParams(pstmt, params); // ��Ԥ��������е�ռλ����ֵ
			rs = pstmt.executeQuery(); // ִ�в�ѯ

			// �����ȡ��������е����� -> ȡ�����������Ǵ浽һ�������У����ں����ѭ��ȡֵ -> ���ȷ������Ĵ�С?
			String[] colNames = this.getColumnNames(rs);
			if(rs.next()) { // ÿ��ѭ���õ�һ������
				map = new HashMap<String, String>();

				// ѭ����ȡÿһ�е�ֵ��ѭ�����е�����������������ȡ��ǰ��һ����һ�е�ֵ
				for (String colName : colNames) {
					map.put(colName, rs.getString(colName));
				}
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return map;
	}

	/**
	 * ��ȡ�ܼ�¼���ķ���  һ��һ��
	 * @param sql Ҫִ�еĲ�ѯ���
	 * @param params Ҫִ�е�sql����ж�Ӧռλ��?��ֵ��������?��˳�������ֵ
	 * @return �ܼ�¼��
	 */
	public int total(String sql, Object ... params) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // ��ȡ����
			pstmt = con.prepareStatement(sql); // Ԥ�������
			this.setParams(pstmt, params); // ��Ԥ��������е�ռλ����ֵ
			rs = pstmt.executeQuery(); // ִ�в�ѯ
			if(rs.next()) { // ÿ��ѭ���õ�һ������
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return result;
	}

	public int total(String sql, List<Object> params) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = this.getConnection(); // ��ȡ����
			pstmt = con.prepareStatement(sql); // Ԥ�������
			this.setParams(pstmt, params); // ��Ԥ��������е�ռλ����ֵ
			rs = pstmt.executeQuery(); // ִ�в�ѯ
			if(rs.next()) { // ÿ��ѭ���õ�һ������
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			
			throw new RuntimeException(e);
		}finally{
			this.close(con, pstmt, rs);
		}
		return result;
	}
}
