package Work;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllInOne {
    WebDriver driver;
    List<WebElement> RedirectUrl = new ArrayList<WebElement>();
    String jsonFromApi;

    int bookingFromApi = 0 ,homeawayFromApi = 0 ,airbnbFromApi = 0, varboFromApi=0;
    int airbnbFrompage = 0;
    int bookingFromPage = 0;
    int homeawayFromPage = 0;
    int vrboFromPage=0;

    int airbnbPercentdiff,homeawayParcentdiff,bookingParcentdiff ,vrboParcentdiff;
    String driverLocation = "/home/w3e-06/Downloads/chromedriver";
    String[] allSites = {
            "RBO", "RH", "SOR",
            "BV", "PET", "STY",
            "EXEC", "OAHU", "ALO",
            "BVAU", "AVI", "BVUK",
            "VHR", "RHDE","MLFR","SR17"
    };

    String []Url={
            "https://www.rentbyowner.com/listing?q=paris",
            "https://www.rentalhomes.com/listing?q=paris",
            "https://www.selloffrentals.com/listing?q=paris",

            "https://www.bedroomvillas.com/listing?q=paris",
            "https://www.Petfriendly.io/listing?q=paris",
            "https://www.Stays.io/listing?q=paris",

            "https://www.execstays.com/listing?q=paris",
            "https://www.oahu.com/listing?q=paris",
            "https://www.alojamiento.io/listing?q=paris",

            "https://www.bedroomvillas.com.au/listing?q=paris",
            "https://www.alohavillas.io/listing?q=paris",
            "https://www.bedroomvillas.co.uk/listing?q=paris",

            "https://www.vacationhome.rent/listing?q=paris",
            "https://www.rentalhomes24.de/listing?q=paris",
            "https://www.meilleureslocations.fr/listing?q=paris",
            "https://www.Summerrentals.io/listing?q=paris"

    };


    @BeforeMethod
    public void  test() throws FileNotFoundException {
        System.setProperty("webdriver.chrome.driver", driverLocation );
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        PrintWriter printer=new PrintWriter(("Allinall.txt"));
        printer.println("| SiteName      |   Partner    | Page No     | Page Ratio  |  Api Ratio | Difference |");
        printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|\n");


    }
    @Test
    public void  DesktopPropertyRatioCompare() throws FileNotFoundException {

        PrintWriter printer=new PrintWriter(new FileOutputStream(new File("Allinall.txt"),true));
        printer.println("| :------------ |------------- DesktopPropertyRatioCompare------------|------------|\n");
        for(int k=0,i=0;i<allSites.length;i++,k++) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://stw.stays.io/v3/get-sts-config/?site="+ allSites[i])
                    .get()
                    .addHeader("authorization", "Basic c3RzOiNzdHNsZWZ0dHJhdmVsIw==")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "af5ec5a0-1ac5-ca22-2b44-e2fb21979e10")
                    .build();
            Response response;

            {
                try {
                    response = client.newCall(request).execute();

                    jsonFromApi = response.body().string();

                    JSONParser parser = new JSONParser();
                    try {

                        JSONObject jsonOb = (JSONObject) parser.parse(jsonFromApi);
                        String setwithratio = jsonOb.get("set_with_ratio").toString();

                        JSONObject ratioOb = (JSONObject) parser.parse(setwithratio);
                        String Stringratio = ratioOb.get("ratio").toString();

                        JSONObject brand_defined_sets_ratioOb = (JSONObject) parser.parse(Stringratio);
                        String Strbrand_defined_sets_ratio = brand_defined_sets_ratioOb.get("brand_defined_sets_ratio").toString();

                        JSONObject defaultOb = (JSONObject) parser.parse(Strbrand_defined_sets_ratio);
                        String strdefault1 = defaultOb.get("default").toString();

                        JSONObject desktopOb = (JSONObject) parser.parse(strdefault1);
                        String desk1 = desktopOb.get("desktop").toString();

                        JSONObject bookingOb = (JSONObject) parser.parse(desk1);

                        bookingFromApi = Integer.parseInt(bookingOb.get("11").toString());
                        airbnbFromApi = Integer.parseInt(bookingOb.get("16").toString());
                        homeawayFromApi = Integer.parseInt(bookingOb.get("12").toString().substring(6, 8));


                        driver.get(Url[k]);
                        try{
                            driver.findElement(By.className("apply-btn")).click();}
                        catch ( Exception e){

                        }
                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));
                        airbnbFrompage=0;
                        bookingFromPage=0;
                        homeawayFromPage=0;

                        for (int j = 0; j < RedirectUrl.size(); j++) {


                            try{

                                if (RedirectUrl.get(j).getAttribute("href").contains("HA-") && RedirectUrl.get(j).getAttribute("href").contains("sf=1")) {
                                    homeawayFromPage++;
                                }
                                if (RedirectUrl.get(j).getAttribute("href").contains("BC-")) {
                                    bookingFromPage++;
                                }
                                if (RedirectUrl.get(j).getAttribute("href").contains("AB-")) {
                                    airbnbFrompage++;
                                }

                            }
                            catch (Exception e){

                            }

                        }

                        System.out.println( i+1 +" : " +allSites[i]);
                        System.out.println("=========1st Page=======");

                        airbnbPercentdiff= (airbnbFrompage*100)/48 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/48 - homeawayFromApi;
                        bookingParcentdiff= (bookingFromPage*100)/48 - bookingFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     1st     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     1st     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|"); }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     1st     |   "+(homeawayFromPage*100)/48 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }


                        airbnbFrompage=0;
                        bookingFromPage=0;
                        homeawayFromPage=0;

                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");


                        try{
                            driver.findElement(By.xpath(".//button[@class='accept_gdpr']")).click();
                            driver.findElement(By.xpath("//a[@data-page='2']")).click();
                        }
                        catch (Exception e) {

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for(int m=0;m<RedirectUrl.size();m++){

                            try{

                                if (RedirectUrl.get(m).getAttribute("href").contains("HA-") && RedirectUrl.get(m).getAttribute("href").contains("sf=1")) {
                                    homeawayFromPage++;
                                }

                                if (RedirectUrl.get(m).getAttribute("href").contains("BC-")) {
                                    bookingFromPage++;
                                }
                                if (RedirectUrl.get(m).getAttribute("href").contains("AB-")) {
                                    airbnbFrompage++;
                                }

                            }
                            catch (Exception e){

                            }
                        }

                        Thread.sleep(20000 );

                        System.out.println(" \n====== 2nd Page=========");
                        airbnbPercentdiff= (airbnbFrompage*100)/48 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/48 - homeawayFromApi;
                        bookingParcentdiff= (bookingFromPage*100)/48 - bookingFromApi;
                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     2nd     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     2nd     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     2nd     |   "+(homeawayFromPage*100)/48 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        airbnbFrompage=0;
                        bookingFromPage=0;
                        homeawayFromPage=0;


                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        printer.close();
    }




    @Test
    public void  DesktopGroupRatio() throws FileNotFoundException {

        PrintWriter printer=new PrintWriter(new FileOutputStream(new File("Allinall.txt"),true));
        printer.println("\n| :------------ |--------------|DesktopGroupRatio---------: |------------|------------|\n");
        for(int k=0,i=0;i<allSites.length;i++,k++){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://stw.stays.io/v3/get-sts-config/?site="+ allSites[i] )
                    .get()
                    .addHeader("authorization", "Basic c3RzOiNzdHNsZWZ0dHJhdmVsIw==")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "af5ec5a0-1ac5-ca22-2b44-e2fb21979e10")
                    .build();
            Response response;

            {
                try {
                    response = client.newCall(request).execute();

                    jsonFromApi = response.body().string();

                    JSONParser parser = new JSONParser();
                    try {

                        JSONObject json = (JSONObject) parser.parse(jsonFromApi);
                        String setwithratio = json.get("ratio_list").toString();

                        JSONObject ratio = (JSONObject) parser.parse(setwithratio);
                        String booking13 = ratio.get("13").toString();

                        JSONObject booking = (JSONObject) parser.parse(booking13);
                        String homeaway1 = booking.get("12").toString();

                        JSONObject home = (JSONObject) parser.parse(homeaway1);

                        bookingFromApi = Integer.parseInt(booking.get("11").toString());
                        airbnbFromApi = Integer.parseInt(booking.get("16").toString());
//                        homeawayFromApi = Integer.parseInt(home.get("1").toString());
                        varboFromApi = Integer.parseInt(home.get("2").toString());



                        driver.get(Url[k]+" &rg=13");
                        try{
                            driver.findElement(By.className("apply-btn")).click();}
                        catch ( Exception e){

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        airbnbFrompage = 0;
                        bookingFromPage = 0;
                        homeawayFromPage = 0;
                        vrboFromPage = 0;

                        for (int j = 0; j < RedirectUrl.size(); j++) {

                            if (RedirectUrl.get(j).getAttribute("href").contains("HA-") && RedirectUrl.get(j).getAttribute("href").contains("sf=1")) {
                                homeawayFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("HA-") && RedirectUrl.get(j).getAttribute("href").contains("sf=2")) {
                                vrboFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        System.out.println( i+1 +" : " + allSites[i]);
                        airbnbPercentdiff = (airbnbFrompage*100)/48 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/48 - homeawayFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/48 - bookingFromApi;
                        vrboParcentdiff = (vrboFromPage*100)/48 - varboFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     1st     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     1st     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     1st     |   "+(homeawayFromPage*100)/48 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(vrboParcentdiff>5  ||vrboParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Vrbo       |     1st     |   " + (vrboFromPage * 100) / 48 + "         |    " + varboFromApi + "      | " + vrboParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }



                        vrboFromPage = 0;
//                        Dimension screenRes = new Dimension(1105, 885);
//                        driver.manage().window().setSize(screenRes);
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");


                        try{
                            driver.findElement(By.xpath(".//button[@class='accept_gdpr']")).click();
                            driver.findElement(By.xpath("//a[@data-page='2']")).click();
                        }
                        catch (Exception e) {

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        airbnbFrompage = 0;
                        bookingFromPage = 0;
                        homeawayFromPage = 0;
                        vrboFromPage = 0;

                        for(int m = 0;m < RedirectUrl.size(); m++){

                            if (RedirectUrl.get(m).getAttribute("href").contains("HA-") && RedirectUrl.get(m).getAttribute("href").contains("sf=1")) {
                                homeawayFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("HA-") && RedirectUrl.get(m).getAttribute("href").contains("sf=2")) {
                                vrboFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        Thread.sleep(20000 );

                        airbnbPercentdiff = (airbnbFrompage*100)/48 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/48 - homeawayFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/48 - bookingFromApi;
                        vrboParcentdiff = (vrboFromPage*100)/48 - varboFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb       |     2nd     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     2nd     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     2nd     |   "+(homeawayFromPage*100)/48 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(vrboParcentdiff>5  ||vrboParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Vrbo       |     2nd     |   " + (vrboFromPage * 100) / 48 + "         |    " + varboFromApi + "      | " + vrboParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        airbnbFrompage = 0;
                        bookingFromPage = 0;
                        homeawayFromPage = 0;
                        vrboFromPage = 0;


                    } catch (ParseException e) {

                    } catch (Exception e) {

                    }


                } catch (IOException e) {

                }
            }
        }
        printer.close();
    }



    @Test
    public void  DesktopSqsHotelRatio() throws FileNotFoundException {

        PrintWriter printer=new PrintWriter(new FileOutputStream(new File("Allinall.txt"),true));

        printer.println("\n| :------------ |--------------DesktopSqsHotelRatio-------: |------------|------------|\n");
        for(int k=0,i=0;i<allSites.length;i++,k++){

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://stw.stays.io/v3/get-sts-config/?site="+ allSites[i])
                    .get()
                    .addHeader("authorization", "Basic c3RzOiNzdHNsZWZ0dHJhdmVsIw==")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "af5ec5a0-1ac5-ca22-2b44-e2fb21979e10")
                    .build();
            Response response;

            {
                try {
                    response = client.newCall(request).execute();

                    jsonFromApi = response.body().string();

                    JSONParser parser = new JSONParser();
                    try {

                        JSONObject json = (JSONObject) parser.parse(jsonFromApi);
                        String setwithratio = json.get("set_with_ratio").toString();

                        JSONObject ratio = (JSONObject) parser.parse(setwithratio);
                        String Stringratio = ratio.get("ratio").toString();

                        JSONObject brand_defined_sets_ratio = (JSONObject) parser.parse(Stringratio);
                        String Strbrand_defined_sets_ratio = brand_defined_sets_ratio.get("brand_defined_sets_ratio").toString();

                        JSONObject default1 = (JSONObject) parser.parse(Strbrand_defined_sets_ratio);
                        String strdefault1 = default1.get("hotels").toString();

                        JSONObject desktop = (JSONObject) parser.parse(strdefault1);
                        String desk1 = desktop.get("mobile").toString();

                        JSONObject booking = (JSONObject) parser.parse(desk1);

                        bookingFromApi = Integer.parseInt(booking.get("11").toString());
                        airbnbFromApi = Integer.parseInt(booking.get("16").toString());


                        driver.get(Url[k]+"&sqs=hotels");
                        try{
                            driver.findElement(By.className("apply-btn")).click();}
                        catch ( Exception e){

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for (int j = 0; j < RedirectUrl.size(); j++) {

                            if (RedirectUrl.get(j).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }
                        System.out.println( i+1 +" : " +allSites[i]);



                        airbnbPercentdiff = (airbnbFrompage*100)/48 - airbnbFromApi;

                        bookingParcentdiff = (bookingFromPage*100)/48 - bookingFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/ 48- bookingFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb       |     1st     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     1st     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        airbnbFrompage = 0;
                        bookingFromPage = 0;


                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");


                        try{
                            driver.findElement(By.xpath(".//button[@class='accept_gdpr']")).click();
                            driver.findElement(By.xpath("//a[@data-page='2']")).click();
                        }
                        catch (Exception e) {

                        }


                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for(int m = 0; m < RedirectUrl.size(); m++){

                            if (RedirectUrl.get(m).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        Thread.sleep(20000 );


                        airbnbPercentdiff = (airbnbFrompage*100)/48 - airbnbFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/48 - bookingFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5){
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     2nd      |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5){
                            printer.println("|"+ allSites[i] +"           |  Booking     |     2nd     |   "+(bookingFromPage*100)/48 +"         |    "+ bookingFromApi +"      | "+bookingParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");}

                        airbnbFrompage = 0;
                        bookingFromPage = 0;


                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        printer.close();
    }




    @Test
    public void  MobilePropertyRatioCompare() throws FileNotFoundException {

        System.setProperty("webdriver.chrome.driver", driverLocation );

        Map<String, String> mobileEmulation = new HashMap<String, String>();

        mobileEmulation.put("deviceName", "Nexus 5");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriver driver = new ChromeDriver(chromeOptions);

        PrintWriter printer=new PrintWriter(new FileOutputStream(new File("Allinall.txt"),true));
        printer.println("\n| :------------ |---- MobilePropertyRatioCompare-: |------------|------------|\n");


        for(int k=0,i=0;i<allSites.length;i++,k++){

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://stw.stays.io/v3/get-sts-config/?site="+ allSites[i])
                    .get()
                    .addHeader("authorization", "Basic c3RzOiNzdHNsZWZ0dHJhdmVsIw==")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "af5ec5a0-1ac5-ca22-2b44-e2fb21979e10")
                    .build();
            Response response;

            {
                try {
                    response = client.newCall(request).execute();

                    jsonFromApi = response.body().string();

                    JSONParser parser = new JSONParser();
                    try {

                        JSONObject jsonOb = (JSONObject) parser.parse(jsonFromApi);
                        String setwithratio = jsonOb.get("set_with_ratio").toString();

                        JSONObject ratioOb = (JSONObject) parser.parse(setwithratio);
                        String Stringratio = ratioOb.get("ratio").toString();

                        JSONObject brand_defined_sets_ratioOb = (JSONObject) parser.parse(Stringratio);
                        String Strbrand_defined_sets_ratio = brand_defined_sets_ratioOb.get("brand_defined_sets_ratio").toString();

                        JSONObject defaultOb = (JSONObject) parser.parse(Strbrand_defined_sets_ratio);
                        String strdefault1 = defaultOb.get("default").toString();

                        JSONObject desktopOb = (JSONObject) parser.parse(strdefault1);
                        String desk1 = desktopOb.get("mobile").toString();

                        JSONObject bookingOb = (JSONObject) parser.parse(desk1);

                        bookingFromApi = Integer.parseInt(bookingOb.get("11").toString());
                        airbnbFromApi = Integer.parseInt(bookingOb.get("16").toString());
                        homeawayFromApi = Integer.parseInt(bookingOb.get("12").toString().substring(6, 8));

//
////                        System.out.println(bookingFromApi);
////                        System.out.println(airbnbFromApi);
//                        System.out.println(homeawayFromApi);

                        driver.get(Url[k]);
                        try{
                            driver.findElement(By.className("apply-btn")).click();}
                        catch ( Exception e){

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for (int j = 0; j < RedirectUrl.size(); j++) {


                            if (RedirectUrl.get(j).getAttribute("href").contains("HA-") && RedirectUrl.get(j).getAttribute("href").contains("sf=1")) {
                                homeawayFromPage++;
                            }

                            if (RedirectUrl.get(j).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        System.out.println( i+1 +" : " +allSites[i]);
                        System.out.println("=========1st Page=======");
                        airbnbPercentdiff= (airbnbFrompage*100)/24 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/24 - homeawayFromApi;
                        bookingParcentdiff= (bookingFromPage*100)/24 - bookingFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     1st     |   "+(airbnbFrompage*100)/24+"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     1st     |   " + (bookingFromPage * 100) / 24 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     1st     |   "+(homeawayFromPage*100)/24 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        airbnbFrompage=0;
                        bookingFromPage=0;
                        homeawayFromPage=0;
                        vrboFromPage=0;



//                        Dimension screenRes = new Dimension(1105, 885);
//                        driver.manage().window().setSize(screenRes);
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");


                        try{
                            driver.findElement(By.xpath(".//button[@class='accept_gdpr']")).click();
                            driver.findElement(By.xpath("//a[@data-page='2']")).click();
                        }
                        catch (Exception e) {

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for(int m=0;m<RedirectUrl.size();m++){


                            if (RedirectUrl.get(m).getAttribute("href").contains("HA-") && RedirectUrl.get(m).getAttribute("href").contains("sf=1")) {
                                homeawayFromPage++;
                            }

                            if (RedirectUrl.get(m).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        Thread.sleep(20000 );

                        System.out.println(" \n====== 2nd Page=========");
                        airbnbPercentdiff= (airbnbFrompage*100)/24 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/24 - homeawayFromApi;
                        bookingParcentdiff= (bookingFromPage*100)/24 - bookingFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     2nd     |   "+(airbnbFrompage*100)/24+"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     2nd     |   " + (bookingFromPage * 100) / 24 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     2nd     |   "+(homeawayFromPage*100)/24 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }


                        airbnbFrompage=0;
                        bookingFromPage=0;
                        homeawayFromPage=0;
                        vrboFromPage=0;


                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        printer.close();
    }




    @Test
    public void  MobileGroupRatio() throws FileNotFoundException {
        System.setProperty("webdriver.chrome.driver", driverLocation );

        Map<String, String> mobileEmulation = new HashMap<String, String>();

        mobileEmulation.put("deviceName", "Nexus 5");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriver driver = new ChromeDriver(chromeOptions);

        PrintWriter printer=new PrintWriter(new FileOutputStream(new File("Allinall.txt"),true));
        printer.println("| :------------ |----------MobileGroupRatio----------: |------------|------------|\n");

        for(int k=0,i=0;i<allSites.length;i++,k++){

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://stw.stays.io/v3/get-sts-config/?site="+ allSites[i])
                    .get()
                    .addHeader("authorization", "Basic c3RzOiNzdHNsZWZ0dHJhdmVsIw==")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "af5ec5a0-1ac5-ca22-2b44-e2fb21979e10")
                    .build();
            Response response;

            {
                try {
                    response = client.newCall(request).execute();

                    jsonFromApi = response.body().string();

                    JSONParser parser = new JSONParser();
                    try {

                        JSONObject json = (JSONObject) parser.parse(jsonFromApi);
                        String setwithratio = json.get("ratio_list").toString();

                        JSONObject ratio = (JSONObject) parser.parse(setwithratio);
                        String booking13 = ratio.get("13").toString();

                        JSONObject booking = (JSONObject) parser.parse(booking13);
                        String homeaway1 = booking.get("12").toString();

                        JSONObject home = (JSONObject) parser.parse(homeaway1);

                        try{
                            bookingFromApi = Integer.parseInt(booking.get("11").toString());
                            airbnbFromApi = Integer.parseInt(booking.get("16").toString());
                            homeawayFromApi = Integer.parseInt(home.get("1").toString());
                            varboFromApi = Integer.parseInt(home.get("2").toString());
                        }
                        catch (Exception e){

                        }


                        driver.get(Url[k]+"&rg=13");
                        try{
                            driver.findElement(By.className("apply-btn")).click();}
                        catch ( Exception e){

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for (int j = 0; j < RedirectUrl.size(); j++) {


                            if (RedirectUrl.get(j).getAttribute("href").contains("HA-") && RedirectUrl.get(j).getAttribute("href").contains("sf=1")) {
                                homeawayFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("HA-") && RedirectUrl.get(j).getAttribute("href").contains("sf=2")) {
                                vrboFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        System.out.println( i+1 +" : " + allSites[i]);
                        airbnbPercentdiff = (airbnbFrompage*100)/24 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/24 - homeawayFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/24 - bookingFromApi;
                        vrboParcentdiff = (vrboFromPage*100)/24 - varboFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     1st     |   "+(airbnbFrompage*100)/24 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     1st     |   " + (bookingFromPage * 100) / 24 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     1st     |   "+(homeawayFromPage*100)/24 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(vrboParcentdiff>5  ||vrboParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Vrbo       |     1st     |   " + (vrboFromPage * 100) / 24 + "         |    " + varboFromApi + "      | " + vrboParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        airbnbFrompage = 0;
                        bookingFromPage = 0;
                        homeawayFromPage = 0;
                        vrboFromPage = 0;
//                        Dimension screenRes = new Dimension(1105, 885);
//                        driver.manage().window().setSize(screenRes);
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");


                        try{
                            driver.findElement(By.xpath(".//button[@class='accept_gdpr']")).click();
                            driver.findElement(By.xpath("//a[@data-page='2']")).click();
                        }
                        catch (Exception e) {

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for(int m = 0;m < RedirectUrl.size(); m++){


                            if (RedirectUrl.get(m).getAttribute("href").contains("HA-") && RedirectUrl.get(m).getAttribute("href").contains("sf=1")) {
                                homeawayFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("HA-") && RedirectUrl.get(m).getAttribute("href").contains("sf=2")) {
                                vrboFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        Thread.sleep(20000 );

                        airbnbPercentdiff = (airbnbFrompage*100)/24 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/24 - homeawayFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/24 - bookingFromApi;
                        vrboParcentdiff = (vrboFromPage*100)/24 - varboFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb       |     2nd     |   "+(airbnbFrompage*100)/24 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     2nd     |   " + (bookingFromPage * 100) /24 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     2nd     |   "+(homeawayFromPage*100)/24 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(vrboParcentdiff>5  ||vrboParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Vrbo       |     2nd     |   " + (vrboFromPage * 100) / 24 + "         |    " + varboFromApi + "      | " + vrboParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        airbnbFrompage = 0;
                        bookingFromPage = 0;
                        homeawayFromPage = 0;
                        vrboFromPage = 0;


                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        printer.close();
    }


    @Test
    public void  MobileSqsHotelRatio() throws FileNotFoundException {

        System.setProperty("webdriver.chrome.driver", driverLocation );

        Map<String, String> mobileEmulation = new HashMap<String, String>();

        mobileEmulation.put("deviceName", "Nexus 5");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().deleteAllCookies();


        PrintWriter printer=new PrintWriter(new FileOutputStream(new File("Allinall.txt"),true));
        printer.println("\n| :------------ |--MobileSqsHotelRatio---------: |------------|------------|\n");


        for(int k=0,i=0;i<allSites.length;i++,k++){

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://stw.stays.io/v3/get-sts-config/?site="+ allSites[i])
                    .get()
                    .addHeader("authorization", "Basic c3RzOiNzdHNsZWZ0dHJhdmVsIw==")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "af5ec5a0-1ac5-ca22-2b44-e2fb21979e10")
                    .build();
            Response response;

            {
                try {
                    response = client.newCall(request).execute();

                    jsonFromApi = response.body().string();

                    JSONParser parser = new JSONParser();
                    try {

                        JSONObject json = (JSONObject) parser.parse(jsonFromApi);
                        String setwithratio = json.get("set_with_ratio").toString();

                        JSONObject ratio = (JSONObject) parser.parse(setwithratio);
                        String Stringratio = ratio.get("ratio").toString();

                        JSONObject brand_defined_sets_ratio = (JSONObject) parser.parse(Stringratio);
                        String Strbrand_defined_sets_ratio = brand_defined_sets_ratio.get("brand_defined_sets_ratio").toString();

                        JSONObject default1 = (JSONObject) parser.parse(Strbrand_defined_sets_ratio);
                        String strdefault1 = default1.get("hotels").toString();

                        JSONObject desktop = (JSONObject) parser.parse(strdefault1);
                        String desk1 = desktop.get("mobile").toString();

                        JSONObject booking = (JSONObject) parser.parse(desk1);

                        bookingFromApi = Integer.parseInt(booking.get("11").toString());
                        airbnbFromApi = Integer.parseInt(booking.get("16").toString());


                        driver.get(Url[k]+"&sqs=hotel");
                        try{
                            driver.findElement(By.className("apply-btn")).click();}
                        catch ( Exception e){

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));
                        airbnbFrompage = 0;
                        bookingFromPage = 0;

                        for (int j = 0; j < RedirectUrl.size(); j++) {

                            if (RedirectUrl.get(j).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        System.out.println( i+1 +" : " +allSites[i]);
                        System.out.println("=========1st Page=======");
                        airbnbPercentdiff = (airbnbFrompage*100)/24 - airbnbFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/24 - bookingFromApi;


                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb       |     1st     |   "+(airbnbFrompage*100)/24 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     1st     |   " + (bookingFromPage * 100) / 24 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        airbnbFrompage = 0;
                        bookingFromPage = 0;

////                        Dimension screenRes = new Dimension(1105, 885);
////                        driver.manage().window().setSize(screenRes);
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");


                        try{
                            driver.findElement(By.xpath(".//button[@class='accept_gdpr']")).click();
                            driver.findElement(By.xpath("//a[@data-page='2']")).click();
                        }
                        catch (Exception e) {

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for(int m = 0; m < RedirectUrl.size(); m++){

                            if (RedirectUrl.get(m).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        Thread.sleep(20000 );

                        System.out.println(" \n====== 2nd Page=========");
                        airbnbPercentdiff = (airbnbFrompage*100)/24 - airbnbFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/24 - bookingFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5){
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     2nd      |   "+(airbnbFrompage*100)/24 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5){
                            printer.println("|"+ allSites[i] +"           |  Booking     |     2nd     |   "+(bookingFromPage*100)/24 +"         |    "+ bookingFromApi +"      | "+bookingParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");}

                        airbnbFrompage = 0;
                        bookingFromPage = 0;


                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        printer.close();
    }


    @Test
    public void  TabletPropertyRatioCompare() throws FileNotFoundException {

        System.setProperty("webdriver.chrome.driver", driverLocation );

        Map<String, String> mobileEmulation = new HashMap<String, String>();

        mobileEmulation.put("deviceName", "iPad");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriver driver = new ChromeDriver(chromeOptions);
        PrintWriter printer=new PrintWriter(new FileOutputStream(new File("Allinall.txt"),true));
        printer.println("\n| :------------ |TabletPropertyRatioCompare--------: |------------|------------|\n");


        for(int k=0,i=0;i<allSites.length;i++,k++){

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://stw.stays.io/v3/get-sts-config/?site="+ allSites[i])
                    .get()
                    .addHeader("authorization", "Basic c3RzOiNzdHNsZWZ0dHJhdmVsIw==")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "af5ec5a0-1ac5-ca22-2b44-e2fb21979e10")
                    .build();
            Response response;

            {
                try {
                    response = client.newCall(request).execute();

                    jsonFromApi = response.body().string();

                    JSONParser parser = new JSONParser();
                    try {

                        JSONObject jsonOb = (JSONObject) parser.parse(jsonFromApi);
                        String setwithratio = jsonOb.get("set_with_ratio").toString();

                        JSONObject ratioOb = (JSONObject) parser.parse(setwithratio);
                        String Stringratio = ratioOb.get("ratio").toString();

                        JSONObject brand_defined_sets_ratioOb = (JSONObject) parser.parse(Stringratio);
                        String Strbrand_defined_sets_ratio = brand_defined_sets_ratioOb.get("brand_defined_sets_ratio").toString();

                        JSONObject defaultOb = (JSONObject) parser.parse(Strbrand_defined_sets_ratio);
                        String strdefault1 = defaultOb.get("default").toString();

                        JSONObject desktopOb = (JSONObject) parser.parse(strdefault1);
                        String desk1 = desktopOb.get("tablet").toString();

                        JSONObject bookingOb = (JSONObject) parser.parse(desk1);

                        bookingFromApi = Integer.parseInt(bookingOb.get("11").toString());
                        airbnbFromApi = Integer.parseInt(bookingOb.get("16").toString());
                        homeawayFromApi = Integer.parseInt(bookingOb.get("12").toString().substring(6, 8));

//
////                        System.out.println(bookingFromApi);
////                        System.out.println(airbnbFromApi);
//                        System.out.println(homeawayFromApi);

                        driver.get(Url[k]);
                        try{
                            driver.findElement(By.className("apply-btn")).click();}
                        catch ( Exception e){

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        airbnbFrompage=0;
                        bookingFromPage=0;
                        homeawayFromPage=0;

                        for (int j = 0; j < RedirectUrl.size(); j++) {


                            if (RedirectUrl.get(j).getAttribute("href").contains("HA-") && RedirectUrl.get(j).getAttribute("href").contains("sf=1")) {
                                homeawayFromPage++;
                            }

                            if (RedirectUrl.get(j).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        System.out.println( i+1 +" : " +allSites[i]);
                        System.out.println("=========1st Page=======");
                        airbnbPercentdiff= (airbnbFrompage*100)/48 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/48 - homeawayFromApi;
                        bookingParcentdiff= (bookingFromPage*100)/48 - bookingFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     1st     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     1st     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     1st     |   "+(homeawayFromPage*100)/48 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        airbnbFrompage=0;
                        bookingFromPage=0;
                        homeawayFromPage=0;

//                        Dimension screenRes = new Dimension(1105, 885);
//                        driver.manage().window().setSize(screenRes);
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");


                        try{
                            driver.findElement(By.xpath(".//button[@class='accept_gdpr']")).click();
                            driver.findElement(By.xpath("//a[@data-page='2']")).click();
                        }
                        catch (Exception e) {

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        try{
                            for(int m=0;m<RedirectUrl.size();m++){


                                if (RedirectUrl.get(m).getAttribute("href").contains("HA-") && RedirectUrl.get(m).getAttribute("href").contains("sf=1")) {
                                    homeawayFromPage++;
                                }
                                if (RedirectUrl.get(m).getAttribute("href").contains("BC-")) {
                                    bookingFromPage++;
                                }
                                if (RedirectUrl.get(m).getAttribute("href").contains("AB-")) {
                                    airbnbFrompage++;
                                }
                            }

                        }
                        catch (Exception e) {

                        }



                        Thread.sleep(20000 );

                        System.out.println(" \n====== 2nd Page=========");
                        airbnbPercentdiff= (airbnbFrompage*100)/48 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/48 - homeawayFromApi;
                        bookingParcentdiff= (bookingFromPage*100)/48 - bookingFromApi;


                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     2nd     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     2nd     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     2nd     |   "+(homeawayFromPage*100)/48 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }


                        airbnbFrompage=0;
                        bookingFromPage=0;
                        homeawayFromPage=0;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        printer.close();
    }


    @Test
    public void  TabletGroupRatio() throws FileNotFoundException {
        System.setProperty("webdriver.chrome.driver", driverLocation );

        Map<String, String> mobileEmulation = new HashMap<String, String>();

        mobileEmulation.put("deviceName", "iPad");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriver driver = new ChromeDriver(chromeOptions);

        PrintWriter printer=new PrintWriter(new FileOutputStream(new File("Allinall.txt"),true));
        printer.println("\n| :------------ |------TabletGroupRatio| ----------: |------------|------------|\n");


        for(int k=0,i=0;i<allSites.length;i++,k++){

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://stw.stays.io/v3/get-sts-config/?site="+ allSites[i])
                    .get()
                    .addHeader("authorization", "Basic c3RzOiNzdHNsZWZ0dHJhdmVsIw==")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "af5ec5a0-1ac5-ca22-2b44-e2fb21979e10")
                    .build();
            Response response;

            {
                try {
                    response = client.newCall(request).execute();

                    jsonFromApi = response.body().string();

                    JSONParser parser = new JSONParser();
                    try {

                        JSONObject json = (JSONObject) parser.parse(jsonFromApi);
                        String setwithratio = json.get("ratio_list").toString();

                        JSONObject ratio = (JSONObject) parser.parse(setwithratio);
                        String booking13 = ratio.get("13").toString();

                        JSONObject booking = (JSONObject) parser.parse(booking13);
                        String homeaway1 = booking.get("12").toString();

                        JSONObject home = (JSONObject) parser.parse(homeaway1);
                        try{
                            bookingFromApi = Integer.parseInt(booking.get("11").toString());
                            airbnbFromApi = Integer.parseInt(booking.get("16").toString());
                            homeawayFromApi = Integer.parseInt(home.get("1").toString());
                            varboFromApi = Integer.parseInt(home.get("2").toString());

                        }
                        catch(Exception e){

                        }


                        driver.get(Url[k]+"&rg=13");
                        try{
                            driver.findElement(By.className("apply-btn")).click();}
                        catch ( Exception e){

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));
                        airbnbFrompage = 0;
                        bookingFromPage = 0;
                        homeawayFromPage = 0;
                        vrboFromPage = 0;


                        for (int j = 0; j < RedirectUrl.size(); j++) {


                            if (RedirectUrl.get(j).getAttribute("href").contains("HA-") && RedirectUrl.get(j).getAttribute("href").contains("sf=1")) {
                                homeawayFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("HA-") && RedirectUrl.get(j).getAttribute("href").contains("sf=2")) {
                                vrboFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        System.out.println( i+1 +" : " + allSites[i]);
                        airbnbPercentdiff = (airbnbFrompage*100)/48 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/48 - homeawayFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/48 - bookingFromApi;
                        vrboParcentdiff = (vrboFromPage*100)/48 - varboFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     1st     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     1st     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     1st     |   "+(homeawayFromPage*100)/48 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(vrboParcentdiff>5  ||vrboParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Vrbo       |     1st     |   " + (vrboFromPage * 100) / 48 + "         |    " + varboFromApi + "      | " + vrboParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        airbnbFrompage = 0;
                        bookingFromPage = 0;
                        homeawayFromPage = 0;
                        vrboFromPage = 0;

                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");


                        try{
                            driver.findElement(By.xpath(".//button[@class='accept_gdpr']")).click();
                            driver.findElement(By.xpath("//a[@data-page='2']")).click();
                        }
                        catch (Exception e) {

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for(int m = 0;m < RedirectUrl.size(); m++){


                            if (RedirectUrl.get(m).getAttribute("href").contains("HA-") && RedirectUrl.get(m).getAttribute("href").contains("sf=1")) {
                                homeawayFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("HA-") && RedirectUrl.get(m).getAttribute("href").contains("sf=2")) {
                                vrboFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        Thread.sleep(20000 );

                        System.out.println(" \n====== 2nd Page=========");
                        airbnbPercentdiff = (airbnbFrompage*100)/48 - airbnbFromApi;
                        homeawayParcentdiff =(homeawayFromPage*100)/48 - homeawayFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/48 - bookingFromApi;
                        vrboParcentdiff = (vrboFromPage*100)/48 - varboFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb       |     2nd     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     2nd     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(homeawayParcentdiff>5 ||homeawayParcentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  HomeAway     |     2nd     |   "+(homeawayFromPage*100)/48 +"         |    "+ homeawayFromApi +"      | "+homeawayParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(vrboParcentdiff>5  ||vrboParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Vrbo       |     2nd     |   " + (vrboFromPage * 100) / 48 + "         |    " + varboFromApi + "      | " + vrboParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        airbnbFrompage = 0;
                        bookingFromPage = 0;
                        homeawayFromPage = 0;
                        vrboFromPage = 0;


                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        printer.close();
    }


    @Test
    public void  TabletSqsHotelRatio() throws FileNotFoundException {

        System.setProperty("webdriver.chrome.driver", driverLocation );

        Map<String, String> mobileEmulation = new HashMap<String, String>();

        mobileEmulation.put("deviceName", "iPad");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.manage().deleteAllCookies();

        PrintWriter printer=new PrintWriter(new FileOutputStream(new File("Allinall.txt"),true));
        printer.println("\n| :------------ |----TabletSqsHotelRatio------: |------------|------------|\n");


        for(int k=0,i=0;i<allSites.length;i++,k++){

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://stw.stays.io/v3/get-sts-config/?site="+ allSites[i])
                    .get()
                    .addHeader("authorization", "Basic c3RzOiNzdHNsZWZ0dHJhdmVsIw==")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "af5ec5a0-1ac5-ca22-2b44-e2fb21979e10")
                    .build();
            Response response;

            {
                try {
                    response = client.newCall(request).execute();

                    jsonFromApi = response.body().string();

                    JSONParser parser = new JSONParser();
                    try {

                        JSONObject json = (JSONObject) parser.parse(jsonFromApi);
                        String setwithratio = json.get("set_with_ratio").toString();

                        JSONObject ratio = (JSONObject) parser.parse(setwithratio);
                        String Stringratio = ratio.get("ratio").toString();

                        JSONObject brand_defined_sets_ratio = (JSONObject) parser.parse(Stringratio);
                        String Strbrand_defined_sets_ratio = brand_defined_sets_ratio.get("brand_defined_sets_ratio").toString();

                        JSONObject default1 = (JSONObject) parser.parse(Strbrand_defined_sets_ratio);
                        String strdefault1 = default1.get("hotels").toString();

                        JSONObject desktop = (JSONObject) parser.parse(strdefault1);
                        String desk1 = desktop.get("tablet").toString();

                        JSONObject booking = (JSONObject) parser.parse(desk1);

                        bookingFromApi = Integer.parseInt(booking.get("11").toString());
                        airbnbFromApi = Integer.parseInt(booking.get("16").toString());


                        driver.get(Url[k]+"&sqs=hotel");
                        try{
                            driver.findElement(By.className("apply-btn")).click();}
                        catch ( Exception e){

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));
                        airbnbFrompage = 0;
                        bookingFromPage = 0;

                        for (int j = 0; j < RedirectUrl.size(); j++) {

                            if (RedirectUrl.get(j).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(j).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        System.out.println( i+1 +" : " +allSites[i]);
                        System.out.println("=========1st Page=======");
                        airbnbPercentdiff = (airbnbFrompage*100)/48 - airbnbFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/48 - bookingFromApi;

                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5) {
                            printer.println("|"+ allSites[i] +"           |  Airbnb       |     1st     |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5) {
                            printer.println("|" + allSites[i] + "           |  Booking     |     1st     |   " + (bookingFromPage * 100) / 48 + "         |    " + bookingFromApi + "      | " + bookingParcentdiff + "        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }

                        airbnbFrompage = 0;
                        bookingFromPage = 0;
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");


                        try{
                            driver.findElement(By.xpath(".//button[@class='accept_gdpr']")).click();
                            driver.findElement(By.xpath("//a[@data-page='2']")).click();
                        }
                        catch (Exception e) {

                        }

                        RedirectUrl = driver.findElements(By.xpath("//*[@class='js-individual-link optimizely-view-button']"));

                        for(int m = 0; m < RedirectUrl.size(); m++){

                            if (RedirectUrl.get(m).getAttribute("href").contains("BC-")) {
                                bookingFromPage++;
                            }
                            if (RedirectUrl.get(m).getAttribute("href").contains("AB-")) {
                                airbnbFrompage++;
                            }
                        }

                        Thread.sleep(20000 );

                        System.out.println(" \n====== 2nd Page=========");
                        airbnbPercentdiff = (airbnbFrompage*100)/48 - airbnbFromApi;
                        bookingParcentdiff = (bookingFromPage*100)/48 - bookingFromApi;


                        if(airbnbPercentdiff>5 ||airbnbPercentdiff < -5){
                            printer.println("|"+ allSites[i] +"           |  Airbnb      |     2nd      |   "+(airbnbFrompage*100)/48 +"         |    "+ airbnbFromApi +"      | "+airbnbPercentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");
                        }
                        if(bookingParcentdiff>5  ||bookingParcentdiff < -5){
                            printer.println("|"+ allSites[i] +"           |  Booking     |     2nd     |   "+(bookingFromPage*100)/48 +"         |    "+ bookingFromApi +"      | "+bookingParcentdiff +"        |");
                            printer.println("| :------------ |--------------| :-----------| ----------: |------------|------------|");}

                        airbnbFrompage = 0;
                        bookingFromPage = 0;


                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        printer.close();
    }




    @AfterMethod
    public void afterTest(){


    }

}
