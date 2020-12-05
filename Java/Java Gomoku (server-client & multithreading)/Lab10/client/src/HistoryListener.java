
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class HistoryListener implements ActionListener {
    GameVisuals gameVisuals;
    private static HttpURLConnection con;

    public HistoryListener(GameVisuals gameVisuals) {
        this.gameVisuals = gameVisuals;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        var url = "http://localhost:8080/rest/game?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6ImpvaG4gZG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.MOLR62N_MdTPa2RyfrnQ0jlQsPMr1yuQaHpbwMJRhPE";
         try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {

                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }


            JFrame history = new JFrame("Finished Games");
            history.setSize(500, 800);
            JTextArea text = new JTextArea();
            text.setLineWrap(true);
            String[] items = content.toString().split("}");
            for(var item:items){
                text.append(item + "}\n\n");
            }
            history.add(text);
            history.setVisible(true);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            con.disconnect();
        }
    }
}
