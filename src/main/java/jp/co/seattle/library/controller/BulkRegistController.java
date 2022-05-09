package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
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

@Controller // APIの入り口
public class BulkRegistController {
	final static Logger logger = LoggerFactory.getLogger(BulkRegistController.class);

	@Autowired
	private BooksService booksService;

	@RequestMapping(value = "/bulkRegist", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String bulkRegist(Model model) {
		return "bulkRegist";
	}

	/**
	 * 書籍情報を登録する
	 * 
	 * @param locale      ロケール情報
	 * @param title       書籍名
	 * @param author      著者名
	 * @param publisher   出版社
	 * @param publishDate 出版日
	 * @param file        サムネイルファイル
	 * @param model       モデル
	 * @return 遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/bulkRegistbook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String bulkRegistBook(Locale locale,
			@RequestParam("file") MultipartFile file, Model model) {
		logger.info("Welcome Books.java! The client locale is {}.", locale);

		// パラメータで受け取った情報をDtoに格納する
		BookDetailsInfo bookInfo = new BookDetailsInfo();
		List<String[]> booksList = new ArrayList<String[]>();
		List<String> errorList = new ArrayList<String>();
		int count = 0;
		String line = null;
		boolean line2 = true;
		// CSVファイルの読み込み
		try {
			InputStream stream = file.getInputStream();
			Reader reader = new InputStreamReader(stream);
			BufferedReader buf = new BufferedReader(reader);

			if ((line2 = buf.ready()) != true) {
				model.addAttribute("errorMessage", "CSVファイルがありません");
				return "bulkRegist";
			}

			while ((line = buf.readLine()) != null) {
				count++;

				final String[] split = line.split(",", -1);

				for (int i = 0; i < split.length; i++) {
					
				}
				// CSVファイルのバリデーションチェック
				String error = booksService.validationcheck(split[0], split[1], split[2], split[3], split[4], model);
				// 書籍Listにセットする
				booksList.add(split);

				// エラーがあった場合、エラーメッセージをセット
				if (!(error.equals(""))) {
					System.out.println(errorList);
					errorList.add(count + "行目の書籍情報でエラーが起きました。<br>");
				}

			}

		} catch (IOException e) {
			System.out.println("k");
			model.addAttribute("CSVに書籍情報がありません。", e);
			return "bulkRegist";

		}

		if (errorList.size() > 0) {
			model.addAttribute("errorMessage", errorList);
			return "bulkRegist";
		}

		for (int i = 0; i < booksList.size(); i++) {
			String[] bookList = booksList.get(i);
			bookInfo.setTitle(bookList[0]);
			bookInfo.setAuthor(bookList[1]);
			bookInfo.setPublisher(bookList[2]);
			bookInfo.setPublishDate(bookList[3]);
			bookInfo.setIsbn(bookList[4]);
			bookInfo.setTexts(bookList[5]);
			System.out.println(bookList[i]);
			booksService.registBook(bookInfo);
		}
		model.addAttribute("bookList", booksService.getBookList());
		return "home";
	}

}
