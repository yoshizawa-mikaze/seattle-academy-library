package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select id, title, author, publisher, publish_date, thumbnail_url from books order by title;",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT *,"
        		+ "case when rent_books.book_id > 0 then '貸出中' else '貸出可' end FROM books left join rent_books "
        		+ "on books.id = rent_books.book_id where books.id ="
                + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }
    
    /**
     * 新規登録した書籍の情報を取得する
     * 
     * 
     * 
     * @return　書籍情報
     */
    
    public BookDetailsInfo insertBookList() {
    	String sql = "SELECT *, case when rent_books.book_id > 0 then '貸出中' else '貸出可' end FROM books left join rent_books on books.id = rent_books.book_id where books.id = (SELECT MAX(id) FROM books);";
    	BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());
    	return bookDetailsInfo;
    }
    /**
     * 
     * 書籍情報の追加、編集時のバリデーションチェック
     *
     * 
     */
    public String validationcheck(String title, String author, String publisher, String publishDate,String isbn, Model model) {
    	 String error = "";
         
         
         
         if(title.equals("") || author.equals("") || publisher.equals("") || publishDate.equals("")) {
         	error += "必須項目を入力してください。<br>";
         } 
         
         if(!publishDate.matches("^[0-9]{4}[0-9]{2}[0-9]{2}$")) {
         	error += "出版日をYYYYMMDD形式にしてください。<br>";
         } 
         
         if(isbn.length() != 0 && !(isbn.matches("^[0-9]{10}|[0-9]{13}"))) {        	
         	error += "ISBNの桁数または半角数字が間違っています。<br>";
         } 
       
    	
		return error;
    	
    }
    

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title, author,publisher,publish_date,thumbnail_name,thumbnail_url,reg_date,upd_date, isbn, texts) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "','"
                + "now()', '"
                + "now()', '"
                + bookInfo.getIsbn() + "','"
                + bookInfo.getTexts() + "' );";

        jdbcTemplate.update(sql);
    }
    
    /**
     * 書籍を編集する
     *
     * @param bookInfo 書籍情報
     * @return 更新した書籍情報
     */
public void editBook(BookDetailsInfo bookInfo) {
	String sql;
	
    	if(bookInfo.getThumbnailUrl() != null) {
    		
	    	 sql = "UPDATE books set title = '" + bookInfo.getTitle() + "', author = '" +  bookInfo.getAuthor() 
	    	+ "', publisher = '"+ bookInfo.getPublisher() 
	    	+ "', publish_date = '" + bookInfo.getPublishDate() 
	    	+ "', thumbnail_name = '" + bookInfo.getThumbnailName() 
	    	+ "' , thumbnail_url = '" + bookInfo.getThumbnailUrl() + "', " 
	    	+ " upd_date = 'now()', " + " isbn = '" + bookInfo.getIsbn() 
	    	+ "', texts = '" + bookInfo.getTexts() 
	    	+ "' where id = " + bookInfo.getBookId() + " ;";
    	} else {
    		sql = "UPDATE books set title = '" + bookInfo.getTitle() + "', author = '" +  bookInfo.getAuthor() 
	    	+ "', publisher = '"+ bookInfo.getPublisher() 
	    	+ "', publish_date = '" + bookInfo.getPublishDate() + "',"
	    	+ " upd_date = 'now()', " + " isbn = '" + bookInfo.getIsbn() 
	    	+ "', texts = '" + bookInfo.getTexts() 
	    	+ "' where id = " + bookInfo.getBookId() + " ;";
    	}
    	
    	
    	jdbcTemplate.update(sql);
    	
    	
    }
    /**
     * 書籍を削除する
     * 
     * @param bookId
     */
    public void deleteBook(Integer bookId) {
    	
    	String sql = "DELETE FROM books Where id = " + bookId + " ;";
    	
    	jdbcTemplate.update(sql);
    	
    }
}
