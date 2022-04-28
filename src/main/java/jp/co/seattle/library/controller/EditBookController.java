package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * 編集コントローラー
 */
@Controller //APIの入り口
public class EditBookController {
    final static Logger logger = LoggerFactory.getLogger(DeleteBookController.class);
    @Autowired
    private BooksService booksService;
    
    @Autowired
    private ThumbnailService thumbnailService;
    
    /**
     * 編集ボタンから編集画面に移るページ
     * @param model
     * @return
     */
    @RequestMapping(value = "/editBook", method = RequestMethod.POST)//value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String editTransition(Model model, int bookId) {
    	model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
        return "editBook";
    }

    
    /**
     * 対象書籍を編集する
     *
     * @param locale ロケール情報
     * @param title 書籍名
     * @param author　著者
     * @param publisher 出版社
     * @param publishDate 出版日
     * @param ISBN 書籍識別番号
     * @param texts 説明文
     * @param model モデル情報
     * @return 遷移先画面名
     */
    @Transactional
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String updateBook(
            Locale locale,
            @RequestParam("bookId") int bookId,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publish_date") String publishDate,
            @RequestParam("isbn") String isbn,
            @RequestParam("texts") String texts,
            @RequestParam("thumbnail") MultipartFile file,
            Model model) {
        logger.info("Welcome update! The client locale is {}.", locale);
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setBookId(bookId);
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setIsbn(isbn);
        bookInfo.setTexts(texts);

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "editBook";
            }
        }

        // 書籍情報を編集して更新する
        
        
       String error = booksService.validationcheck(title, author, publisher, publishDate, isbn, model);
        if(!(error.equals(""))) {
        	model.addAttribute("error", error);
        	 bookInfo.setThumbnailName("null");
             bookInfo.setThumbnailUrl("null");
        	model.addAttribute("bookDetailsInfo", bookInfo);
        	return "editBook";
        	
        } 
        booksService.editBook(bookInfo);
        
        //更新した書籍の詳細情報を表示するように実装
        //  詳細画面に遷移する
        BookDetailsInfo bookDetailsInfo = booksService.getBookInfo(bookInfo.getBookId());
        model.addAttribute("bookDetailsInfo", bookDetailsInfo);
        return "details";

    }

}
