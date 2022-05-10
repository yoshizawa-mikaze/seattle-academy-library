package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 
 * 書籍貸出サービス
 *
 */
@Service
public class RentBooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 書籍IDに紐づく書籍詳細情報を貸出リストから取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public int getRentBookInfo(int bookId) {
    	String sql = "SELECT COUNT(*) FROM rent_books where book_id = ?";

    	int rentId = jdbcTemplate.queryForObject(sql, int.class,bookId);
  				
        return rentId;
    }
    /**
     * 
     * 書籍を貸出テーブルに登録する
     * 
     * @param bookId 書籍ID
     * 
     */
    public void insertRentBook(int bookId) {
    	String sql = "INSERT INTO rent_books (book_id) VALUES (?) ;";
    	
    	jdbcTemplate.update(sql,bookId);
    }
   /**
    * 
    * 書籍を貸出テーブルから削除する
    * 
    * @param bookId　書籍ID
    */
    public void returnBook(int bookId) {
    	String sql = "DELETE FROM rent_books where book_id = ?;";
    	
    	jdbcTemplate.update(sql,bookId);
    }
}
