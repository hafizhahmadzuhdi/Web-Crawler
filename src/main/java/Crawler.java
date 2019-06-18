import java.io.IOException;
import java.util.*;

public class Crawler {

    private int MAX_PAGES;
    private Set<String> pages_visited= new HashSet<>();
    //private ArrayList<String> pages_to_visit;
    private List<String> pagesToVisit = new LinkedList<>();
    private static final String URL_PATTERN = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public Crawler(){

    }
    /**
     * Our main launching point for the Spider's functionality. It creates spider legs
     * that make an HTTP request and parse the response (the web page).
     *
     * @param url        - The starting point of the spider
     * @param searchWord - The word or string that you are searching for
     */
    public void loopUrl(String url,String searchWord)throws IOException{
        if (url == null || searchWord == null) {
            throw new IllegalArgumentException("Arguments should not be null");
        } else if (!url.matches(URL_PATTERN)) {
            throw new IOException("The url is not valid");
        }
    }

    public int getUrlSize(){
        return 0;
    }
    /**
     * Returns the next URL to visit (in the order that they were found). We also do a check to make
     * sure this method doesn't return a URL that has already been visited.
     *
     * @return
     */
    public String nextUrl(){
        String nextUrl = null;
        if (nextUrl == ""){
            throw new NullPointerException("Page should not be empty");
        }
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while (this.pages_visited.contains(nextUrl));
        this.pages_visited.add(nextUrl);

        return nextUrl;
    }
}
