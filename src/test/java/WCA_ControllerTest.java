import com.google.gson.Gson;
import exception.AllowedWritersNumberExceededException;
import exception.AttributeNotPresentedException;
import exception.MovieYearShouldBeLessThanOrEqualCurrentYearException;
import exception.YearException;
import junit.framework.TestCase;
import models.Book;
import models.Movie;
import models.Music;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;
import org.junit.runners.model.TestTimedOutException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.rules.Timeout.builder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WCA_ControllerTest {

    @Before
    public void setUp() {

    }


    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Rule
    public Timeout timeout = Timeout.seconds(5);

    @Test
    public void getAllReturnNonEmptyResponse() throws YearException, IOException {
        // Arrange
        Scraper scraper = new Scraper();
        Crawler crawler = new Crawler();
        Content content = new Content();
        String url = "http://localhost/sample_site_to_crawl/catalog.php";

        WCA_Controller controller = new WCA_Controller(scraper, crawler, content);

        /*// expectations
        when(content.converToJson()).thenReturn(jsonResponse);*/

        // Act
        String response = controller.getAll(url);

        // Assert
        assertNotNull("Response is null", response);
        assertFalse("Response is empty", response.trim().isEmpty());
    }

    @Test
    public void getAllThrowsYearException() throws YearException, IOException {
        exception.expect(YearException.class);
        exception.expectMessage(containsString("year"));

        // Arrange
        String url = "";
        String jsonResponse = "Test";

        WCA_Controller controller = mock(WCA_Controller.class);

        // expectations
        when(controller.getAll(anyString())).thenThrow(new YearException("Invalid year"));

        // Act
        String response = controller.getAll(url);
    }

    @Test
    public void getAllReturnValidResponse() throws YearException, IOException {
        // Arrange
        Scraper scraper = new Scraper();
        Crawler crawler = new Crawler();
        Content content = new Content();
        String url = "http://localhost/sample_site_to_crawl/catalog.php";

        ArrayList<Book> books = new ArrayList<>();
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<Music> musics = new ArrayList<>();

        WCA_Controller controller = new WCA_Controller(scraper, crawler, content);

        List<String> writers = new ArrayList<>(Arrays.asList("J.R.R. Tolkien", "Fran Walsh", "Philippa Boyens"));
        List<String> stars = new ArrayList<>(Arrays.asList("Ron Livingston", "Jennifer Aniston", "Ali", "Ahmed"));
        List<String> authors = new ArrayList<>(Arrays.asList("author 1", "author 2"));

        books.add(new Book(1, "How to code in Java", "Computer", "pdf", 2009, authors, "Gramedia Publisher", "ISBN123456789"));
        movies.add(new Movie(1, "The Princess Bride", "Drama", "Blue-ray", 2001, "Peter Jackson", writers, stars));
        musics.add(new Music(1, "genre1", "format", 2011, ("artist1"), "title"));

        content.setBooks(books);
        content.setMovies(movies);
        content.setMusics(musics);

        // Act
        String response = controller.getAll(url);
        System.out.println(response);

        // Assert
        assertNotNull("Response is null", response);
        assertFalse("Response is empty", response.trim().isEmpty());
        assertTrue(response.contains("movies"));
        assertTrue(response.contains("books"));
        assertTrue(response.contains("music"));
        assertTrue(response.contains("The Princess Bride"));
    }

    @Test
    public void getSpecificReturnNonEmptyResponse() throws YearException, IOException {
        // Arrange
        Scraper scraper = new Scraper();
        Crawler crawler = new Crawler();
        Content content = new Content();

        //String url = "http://localhost/sample_site_to_crawl/details.php?id=204";
        String url = "http://localhost/sample_site_to_crawl/catalog.php";
        String keyword = "Princess";

        WCA_Controller controller = new WCA_Controller(scraper, crawler, content);
        /*ArrayList<Book> books = new ArrayList<>();
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<Music> musics = new ArrayList<>();



        List<String> writers = new ArrayList<>(Arrays.asList("J.R.R. Tolkien", "Fran Walsh", "Philippa Boyens"));
        List<String> stars = new ArrayList<>(Arrays.asList("Ron Livingston", "Jennifer Aniston", "Ali", "Ahmed"));
        List<String> authors = new ArrayList<>(Arrays.asList("author 1", "author 2"));

        books.add(new Book(1, "How to code in Java", "Computer", "pdf", 2009, authors, "Gramedia Publisher", "ISBN123456789"));
        movies.add(new Movie(1, "The Princess Bride", "Drama", "Blue-ray", 2001, "Peter Jackson", writers, stars));
        musics.add(new Music(1, "genre1", "format", 2011, ("artist1"), "title"));

        content.setBooks(books);
        content.setMovies(movies);
        content.setMusics(musics);*/


        // Act
        String response = controller.getSpecific(url, keyword);
        System.out.println(response);

        // Assert
        assertNotNull("Response is null", response);
        assertFalse("Response is empty", response.trim().isEmpty());
    }

    @Test
    public void endTimeShouldBeAfterStartTime() throws YearException, IOException {
        // Arrange
        Scraper scraper = new Scraper();
        Crawler crawler = new Crawler();
        Content content = new Content();
        Date startTime;
        Date endTime;

        String url = "http://localhost/sample_site_to_crawl/details.php?id=204";
        String keyword = "Princess";

        ArrayList<Book> books = new ArrayList<>();
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<Music> musics = new ArrayList<>();

        WCA_Controller controller = new WCA_Controller(scraper, crawler, content);

        List<String> writers = new ArrayList<>(Arrays.asList("J.R.R. Tolkien", "Fran Walsh", "Philippa Boyens"));
        List<String> stars = new ArrayList<>(Arrays.asList("Ron Livingston", "Jennifer Aniston", "Ali", "Ahmed"));
        List<String> authors = new ArrayList<>(Arrays.asList("author 1", "author 2"));

        books.add(new Book(1, "How to code in Java", "Computer", "pdf", 2009, authors, "Gramedia Publisher", "ISBN123456789"));
        movies.add(new Movie(1, "The Princess Bride", "Drama", "Blue-ray", 2001, "Peter Jackson", writers, stars));
        musics.add(new Music(1, "genre1", "format", 2011, ("artist1"), "title"));

        content.setBooks(books);
        content.setMovies(movies);
        content.setMusics(musics);


        // Act
        String response = controller.getSpecific(url, keyword);
        startTime = controller.getStartTime();
        endTime = controller.getEndTime();
        System.out.println("end time:" + endTime.getTime() + " -- " + " start time:" + startTime.getTime());

        // Assert
        assertNotNull("Response is null", response);
        assertFalse("Response is empty", response.trim().isEmpty());
        collector.checkThat(endTime, greaterThan(startTime));
    }


    @Test
    public void getAllCompletedInTime() throws YearException, IOException, InterruptedException, Throwable {

        Thread.sleep(2000);

        // Arrange
        Scraper scraper = new Scraper();
        Crawler crawler = new Crawler();
        Content content = new Content();
        String url = "http://localhost/sample_site_to_crawl/details.php?id=204";

        ArrayList<Book> books = new ArrayList<>();
        ArrayList<Movie> movies = new ArrayList<>();
        ArrayList<Music> musics = new ArrayList<>();

        WCA_Controller controller = new WCA_Controller(scraper, crawler, content);

        List<String> writers = new ArrayList<>(Arrays.asList("J.R.R. Tolkien", "Fran Walsh", "Philippa Boyens"));
        List<String> stars = new ArrayList<>(Arrays.asList("Ron Livingston", "Jennifer Aniston", "Ali", "Ahmed"));
        List<String> authors = new ArrayList<>(Arrays.asList("author 1", "author 2"));

        books.add(new Book(1, "How to code in Java", "Computer", "pdf", 2009, authors, "Gramedia Publisher", "ISBN123456789"));
        movies.add(new Movie(1, "The Princess Bride", "Drama", "Blue-ray", 2001, "Peter Jackson", writers, stars));
        musics.add(new Music(1, "genre1", "format", 2011, ("artist1"), "title"));

        content.setBooks(books);
        content.setMovies(movies);
        content.setMusics(musics);

        // Act
        String response = controller.getAll(url);
        System.out.println(response);

        // Assert
        assertNotNull("Response is null", response);
        assertFalse("Response is empty", response.trim().isEmpty());
        assertTrue(response.contains("movies"));
        assertTrue(response.contains("books"));
        assertTrue(response.contains("music"));
    }
}
