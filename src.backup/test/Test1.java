package test;
import com.google.gson.Gson;
import com.yc.vh.dao.DBHelper;
import org.junit.Test;
import java.util.List;
import java.util.Map;

/**
 * @author HP
 */
public class Test1 {
	
	@Test
	public void add() {
		DBHelper db = new DBHelper();
		int id = 2;
		String sql = "select id, name, translation, soundFilePath from sentences where vocabulary_id = ?";
		List<Map<String, String>> result = db.finds(sql, id);
	
		
		Gson gson = new Gson();
		System.out.print(gson.toJson(result));
	}
}
