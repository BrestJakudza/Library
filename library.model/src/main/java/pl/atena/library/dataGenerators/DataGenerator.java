package pl.atena.library.dataGenerators;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.fluttercode.datafactory.impl.DataFactory;

import pl.atena.library.model.Book;
import pl.atena.library.model.User;

public class DataGenerator {

	private List<String> names;
	private List<String> surnames;
	private List<String> titles;
	private final String LOREIPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

	public Book generateTestBook() {
		return generateBook();
	}

	private Book generateBook() {
		Book book = new Book();
		DataFactory df = new DataFactory();
		book.setTitle(df.getBusinessName());
		book.setAuthor(df.getName());
		book.setDescription(LOREIPSUM);
		book.setNrOfPages(Integer.valueOf((int) Math.round(Math.random() * 100)));
		book.setPublicationDate(generateDate());
		System.out.format("\r\n++++++ Generated book: %s\r\n", book);
		return book;
	}

	public List<Book> generateBooks(Integer count) {
		List<Book> result = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			result.add(generateBook());
		}
		return result;
	}

	@SuppressWarnings("unused")
	private String generateTitle() {
//		if (this.titles == null) {
//			setTitles(loadFromFile("books.txt"));
//		}
//		Random rnd = new Random();
//		return titles.get(rnd.nextInt(titles.size()));
		DataFactory df = new DataFactory();
		return df.getRandomWord(10, 30);
	}

	public User generateTestUser() {
		return generateUserByDataFactory();
	}

	public List<User> generateUsers(Integer count) {
		List<User> result = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			// result.add(generateUser());
			result.add(generateUserByDataFactory());
		}
		return result;
	}

	private User generateUserByDataFactory() {
		DataFactory df = new DataFactory();
		User result = new User();
		result.setName(df.getFirstName());
		result.setSurname(df.getLastName());
		result.setEmail(result.getName() + "@mail.pl");
		System.out.format("Generated user: %s\r\n", result);
		return result;
	}

	@SuppressWarnings("unused")
	private User generateUser() {
		User result = new User();
		result.setName(generateName());
		result.setSurname(generateSurname());
		System.out.format("Generated user: %s\r\n", result);
		return result;
	}

	private String generateSurname() {
		if (surnames == null) {
			setSurnames(loadFromFile("lastNames.txt"));
		}
		return surnames.get(new Random().nextInt(surnames.size()));
	}

	private String generateName() {
		if (names == null) {
			setNames(loadFromFile("names.txt"));
		}
		return names.get(new Random().nextInt(names.size()));
	}

	private List<String> loadFromFile(String fileName) {
		List<String> result = new ArrayList<>();
		Scanner s = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
		    URL resource = classLoader.getResource("data/" + fileName);
		    File file = new File(resource.getFile());
			s = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (s.hasNextLine()) {
			result.add(s.nextLine());
		}
		s.close();
		System.out.format("Loads %d %s\r\n", result.size(), fileName);
		return result;
	}

	private Date generateDate() {
		DataFactory df = new DataFactory();
		Date minDate = df.getDate(2000, 1, 1);
		Date maxDate = new Date();
		Date start = df.getDateBetween(minDate, maxDate);
		System.out.println("Date = " + start);
		return start;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<String> getSurnames() {
		return surnames;
	}

	public void setSurnames(List<String> surnames) {
		this.surnames = surnames;
	}

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

}
