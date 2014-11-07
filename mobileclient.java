import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.io.*;
import java.io.*;


public class mobileclient extends MIDlet implements CommandListener {
    // display manager
    Display display = null;
    
    // a menu with items
    List menu = null; // main menu

    String choice;
    String url = "http://localhost:8080/servlet/remoteServlet";
    // command
    private Alert alStatus;
    private String opName = null;
    private TextBox tbMain = new TextBox("Status", "",75, TextField.ANY);
    static final Command backCommand = new Command("Back", Command.BACK, 0);
    static final Command mainMenuCommand = new Command("Main", Command.SCREEN, 1);
    static final Command exitCommand = new Command("Exit", Command.STOP, 2);
    private static final int ALERT_DISPLAY_TIME = 5000;
    String currentMenu = null;

    // constructor.
    public mobileclient() {
    }

    public void startApp() throws MIDletStateChangeException {
      display = Display.getDisplay(this);

      menu = new List("Operations Menu", Choice.IMPLICIT);
      menu.append("Multiply", null);
      menu.append("Addition", null);
      menu.append("Subtraction", null);
      menu.addCommand(exitCommand);
      menu.setCommandListener(this);
      mainMenu();
    }

    public void pauseApp() {
      display = null;
      menu = null;
    }

    public void destroyApp(boolean unconditional) {
      notifyDestroyed();
    }

    // main menu
    void mainMenu() {
      display.setCurrent(menu);
      currentMenu = "Main"; 
    }

    
    
    public void mul() {
	choice = "0";
       showAlert("Submitting - " + opName, false, tbMain);
        opName = "Multiplication";
      subtask st = new subtask(url,this, opName,choice);
          st.start();
      currentMenu = "Multiply";
    }
   
    public void add() {
	choice = "1";
        opName = "Addition";
       showAlert("Submitting - " + opName, false, tbMain);
       subtask st = new subtask(url,this, opName,choice);
          st.start();
          currentMenu = "Addition"; 
    }

    public void sub() {
	choice = "2";
        opName = "Subtraction";
       showAlert("Submitting - " + opName, false, tbMain);
       subtask st = new subtask(url,this, opName,choice);
          st.start();
       currentMenu = "Subtraction"; 
    }
   public void showResult(String result)
      {
          showAlert(result,true, menu);
          //display.setCurrent(tbMain);
      }

   public void commandAction(Command c, Displayable d) {
      String label = c.getLabel();
      if (label.equals("Exit")) {
         destroyApp(true);
      } else if (label.equals("Back")) {
          if(currentMenu.equals("Multiply") || currentMenu.equals("Addition") || currentMenu.equals("Subtrction")) {
            // go back to menu
            mainMenu();
          } 
      } else {
         List down = (List)display.getCurrent();
         switch(down.getSelectedIndex()) {
           case 0: mul();break;
           case 1: add();break;
           case 2: sub();break;
         }
      }
  }
   public void showAlert(String msg, boolean modal, Displayable 
         displayable)
      {
         // Create alert, add text, associate a sound
        alStatus = new Alert("Status", msg, null, AlertType.INFO);

        // Set the alert type
        if (modal)
          alStatus.setTimeout(Alert.FOREVER);
        else
          alStatus.setTimeout(ALERT_DISPLAY_TIME);

        // Show the alert, followed by the displayable
        display.setCurrent(alStatus, displayable);
      }
}


class subtask implements Runnable
    {
      private String url;
      private mobileclient MIDlet;
      private String opName = null;
      private String choice;
      private boolean taskSuccess = false;

      public subtask(String url, mobileclient MIDlet, String opName,String choice)
      {
        this.url = url;
        this.MIDlet = MIDlet;
        this.opName = opName;
        this.choice=choice;
      }

      public void run()
      {
        try
        {
          prepare(url,choice);
        }
        catch (Exception e)
        {
          System.err.println("Msg: " + e.toString());
        }
      }

      public void start()
      {
        Thread thread = new Thread(this);
        try
        {
          thread.start();
        }
        catch (Exception e)
        {
        }
      }
      public void prepare(String url,String choice) {
        HttpConnection c = null;
        InputStream is = null;
        OutputStream os = null;
        StringBuffer b = new StringBuffer();
        TextBox t = null;
        try {
          c = (HttpConnection)Connector.open(url);
          c.setRequestMethod(HttpConnection.POST);
          c.setRequestProperty("IF-Modified-Since", "20 Oct 2001 16:19:14 GMT");
          c.setRequestProperty("User-Agent","Profile/MIDP-1.0 Configuration/CLDC-1.0");
          c.setRequestProperty("Content-Language", "en-CA");
          c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
          
          os = c.openOutputStream();
          // encode data
          os.write(("choice="+choice).getBytes());
          os.flush();

          is = c.openDataInputStream();
          int ch;
          while ((ch = is.read()) != -1) {
            b.append((char) ch);
            System.out.print((char)ch);
          }
          String result;
          result=b.toString();
          MIDlet.showResult(result);
          
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
           if(is!= null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
           }
           if(os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
           }
           if(c != null) {
                try {
                    c.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
           }
        }
        
    }
}
      
