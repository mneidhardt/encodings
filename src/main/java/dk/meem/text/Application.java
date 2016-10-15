package dk.meem.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class Application {

    public static void main( String[] args ) {
    	String incharset = Charset.defaultCharset().name();
    	String outfilename = null;
    	String outcharset = null;

    	try {
    		if (args.length == 0) {
    			throw new IOException("Filename missing.");
    		}
    		String infilename = args[0];
    		
    		if (args.length > 1) {
    			incharset = args[1];
    			
    			if (args.length > 2) {
    				outfilename = args[2];
    				outcharset = "UTF-8";
    				
    				if (args.length > 3) {
    					outcharset = args[3];
    				}
    			}
    		}
        	new Application().run(infilename, incharset, outfilename, outcharset);
    	} catch (Exception e) {
    		System.out.println("Reads a file in a given encoding, and prints out the contents.");
    		System.out.println("If you supply an outfilename, will print data from inputfile to outfile.");
    		System.out.println("Outputencodingcharset is either what you specify, or UTF-8 if none given.\n");
    		System.out.println("Args: infilename [inputencodingcharset [outfilename [outputencodingcharset]]]");
    		System.out.println("inputencodingcharset: E.g 'UTF-8'. Defaults to the JVM default charset.");
    		System.out.println("outputencodingcharset: E.g 'UTF-8'. Defaults to UTF-8.");
    		System.out.println("incharset: " + incharset);
    		if (outfilename != null) {
    			System.out.println("outcharset: " + outcharset);
    		}
    	}
    }
    
    public void run(String infilename, String incharset, String outfilename, String outcharset) throws IOException, FileNotFoundException, UnsupportedEncodingException {
    	System.out.println("#Using inputencodingcharset " + incharset + ".");
    	if (outfilename != null) {
        	System.out.println("#Using outputencodingcharset " + outcharset + ".");
    	}

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(infilename), incharset));

		String line;

		while ((line = br.readLine()) != null) {
			if (outfilename != null) {
				FileUtils.writeStringToFile(new File(outfilename), line + System.getProperty("line.separator"), outcharset, true);
			} else {
				System.out.println(line);
			}
		}

		br.close();
    }

    public String containsCharsAbove(String line, int lowerbound) {
		String n = Normalizer.normalize(line.trim(), Normalizer.Form.NFC);

		char[] chars = n.toCharArray();
		String hits = "";

		for (int i = 0; i < chars.length; i++) {
			int cp = Character.codePointAt(chars, i);
			if (cp > lowerbound) {
				hits += "[" + chars[i] + "=" + cp + " @" + (i + 1) + "] ";
			}
		}
		
		return hits;
    }

    public int containsSpaces(String line) {
    	Pattern pattern = Pattern.compile("\\s{2}");
        Matcher matcher = pattern.matcher(line);
        int found = 0;
        while (matcher.find()) {
        	++found;
        }
        
        return found;
    }
}
