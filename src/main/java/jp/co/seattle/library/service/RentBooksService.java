package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.LendBookInfo;
import jp.co.seattle.library.rowMapper.LendBookInfoRowMapper;

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
    public List<LendBookInfo> getRentBookList() {
    	String sql= "select rent_books.id,rent_books.book_id,books.title,rent_books.rent_date,rent_books.return_date from books left join rent_books on books.id=rent_books.book_id;";
     
        List<LendBookInfo> getedRentBookList = jdbcTemplate.query(sql,new LendBookInfoRowMapper());

        return getedRentBookList;
    }
    
    /**
     * 書籍IDに紐づく書籍詳細情報を貸出リストから取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public LendBookInfo getRentBookInfo(int bookId) {
    	try {
    	String sql = "select rent_books.id,rent_books.book_id,books.title,rent_books.rent_date,rent_books.return_date from books left join rent_books on books.id=rent_books.book_id where rent_books.book_id=?;";
    	
        LendBookInfo rentRecord = jdbcTemplate.queryForObject(sql,new LendBookInfoRowMapper(),bookId);		
        return rentRecord;
        
    	} catch(Exception e) {
    		return null;
    	}
        
    }
    /**
     * 
     * 書籍を貸出し、貸出日を登録する
     * 
     * @param bookId 書籍ID
     * @param title 書籍名
     * 
     */
    public void insertRentBook(int bookId) {
    	String sql="INSERT INTO rent_books (book_id,rent_date) VALUES (?,now()) ;";
    		
    	jdbcTemplate.update(sql,bookId);
    }
    /**
     * 
     * 書籍貸出時に貸出日を更新する
     * 
     * 
     */
    public void updateRentBook() {
    	String sql="UPDATE rent_books set return_date=null, rent_date=now()";
    	
    	jdbcTemplate.update(sql);
    }
   /**
    * 
    * 書籍を返却し、返却日を更新する
    * 
    * @param bookId　書籍ID
    */
    public void returnBook(int bookId) {
    	String sql = "update rent_books set rent_date=null,return_date=now() where book_id=?;";

    	jdbcTemplate.update(sql,bookId);
    }
}
