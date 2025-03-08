import javax.swing.*;
import java.awt.*;
import java.sql.*;


public class MainFrame extends JFrame {

    private Connection connection;

    private String username;

    public MainFrame(String username) {
        this.username = username;

        setTitle("Main Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/DataBaseName", "root", "YourPasswordHere");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Add Visit", createAddVisitPanel(username));
        tabbedPane.addTab("Delete Visit", createDeleteVisitPanel());
        tabbedPane.addTab("Display/Edit/Update", createDisplayEditUpdatePanel());
        tabbedPane.addTab("Display Sorted Countries (Food)", createDisplaySortedCountriesPanel());
        tabbedPane.addTab("Display Image", createDisplayImagePanel());
        tabbedPane.addTab("Display Visits by Year", createDisplayVisitsByYearPanel());
        tabbedPane.addTab("Display Most Visited Countries", createDisplayMostVisitedCountriesPanel());
        tabbedPane.addTab("Display Spring Visits", createDisplaySpringVisitsPanel());
        tabbedPane.addTab("Share Visit", createShareVisitPanel(username));
        tabbedPane.addTab("Shared Visits With Me", createSharedVisitsWithMePanel(username));

        add(tabbedPane);

        setVisible(true);
    }

    private JPanel createAddVisitPanel(String username) {
        JPanel panel = new JPanel(new GridLayout(8, 2));

        JTextField countryField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField seasonField = new JTextField();
        JTextField featureField = new JTextField();
        JTextField commentsField = new JTextField();
        JTextField ratingField = new JTextField();
        JButton addButton = new JButton("Add Visit");

        panel.add(new JLabel("Country:"));
        panel.add(countryField);
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Season:"));
        panel.add(seasonField);
        panel.add(new JLabel("Best Feature:"));
        panel.add(featureField);
        panel.add(new JLabel("Comments:"));
        panel.add(commentsField);
        panel.add(new JLabel("Rating:"));
        panel.add(ratingField);
        panel.add(new JPanel());
        panel.add(addButton);

        addButton.addActionListener(e -> {
            String country = countryField.getText();
            String city = cityField.getText();
            int year = Integer.parseInt(yearField.getText());
            String season = seasonField.getText();
            String feature = featureField.getText();
            String comments = commentsField.getText();
            int rating = Integer.parseInt(ratingField.getText());

            try {
                String query = "INSERT INTO visits (username, country_name, city_name, year_visited, season_visited, best_feature, comments, rating) " +
                                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, country);
                statement.setString(3, city);
                statement.setInt(4, year);
                statement.setString(5, season);
                statement.setString(6, feature);
                statement.setString(7, comments);
                statement.setInt(8, rating);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Visit added successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding visit.");
            }
        });

        return panel;
    }

    private JPanel createDeleteVisitPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));

        JTextField visitIdField = new JTextField();
        JButton deleteButton = new JButton("Delete Visit");

        panel.add(new JLabel("Visit ID:"));
        panel.add(visitIdField);
        panel.add(new JPanel());
        panel.add(deleteButton);

        deleteButton.addActionListener(e -> {
            int visitId = Integer.parseInt(visitIdField.getText());

            try {
                String query = "DELETE FROM visits WHERE visit_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, visitId);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Visit deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Visit not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting visit.");
            }
        });

        return panel;
    }

    private JPanel createDisplayEditUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout());


        JTextField visitIdField = new JTextField();
        JButton loadButton = new JButton("Load Visit");
        JButton updateButton = new JButton("Update Visit");

        JTextField countryField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField seasonField = new JTextField();
        JTextField featureField = new JTextField();
        JTextField commentsField = new JTextField();
        JTextField ratingField = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(9, 2));
        inputPanel.add(new JLabel("Visit ID:"));
        inputPanel.add(visitIdField);
        inputPanel.add(new JPanel());
        inputPanel.add(loadButton);
        inputPanel.add(new JLabel("Country:"));
        inputPanel.add(countryField);
        inputPanel.add(new JLabel("City:"));
        inputPanel.add(cityField);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);
        inputPanel.add(new JLabel("Season:"));
        inputPanel.add(seasonField);
        inputPanel.add(new JLabel("Best Feature:"));
        inputPanel.add(featureField);
        inputPanel.add(new JLabel("Comments:"));
        inputPanel.add(commentsField);
        inputPanel.add(new JLabel("Rating:"));
        inputPanel.add(ratingField);
        inputPanel.add(new JPanel());
        inputPanel.add(updateButton);

        panel.add(inputPanel, BorderLayout.CENTER);

        loadButton.addActionListener(e -> {
            int visitId = Integer.parseInt(visitIdField.getText());

            try {
                String query = "SELECT * FROM visits WHERE visit_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, visitId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    countryField.setText(resultSet.getString("country_name"));
                    cityField.setText(resultSet.getString("city_name"));
                    yearField.setText(String.valueOf(resultSet.getInt("year_visited")));
                    seasonField.setText(resultSet.getString("season_visited"));
                    featureField.setText(resultSet.getString("best_feature"));
                    commentsField.setText(resultSet.getString("comments"));
                    ratingField.setText(String.valueOf(resultSet.getInt("rating")));
                } else {
                    JOptionPane.showMessageDialog(this, "Visit not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading visit.");
            }
        });

        updateButton.addActionListener(e -> {
            int visitId = Integer.parseInt(visitIdField.getText());
            String country = countryField.getText();
            String city = cityField.getText();
            int year = Integer.parseInt(yearField.getText());
            String season = seasonField.getText();
            String feature = featureField.getText();
            String comments = commentsField.getText();
            int rating = Integer.parseInt(ratingField.getText());

            try {
                String query = "UPDATE visits SET country_name = ?, city_name = ?, year_visited = ?, season_visited = ?, best_feature = ?, comments = ?, rating = ? WHERE visit_id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, country);
                statement.setString(2, city);
                statement.setInt(3, year);
                statement.setString(4, season);
                statement.setString(5, feature);
                statement.setString(6, comments);
                statement.setInt(7, rating);
                statement.setInt(8, visitId);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Visit updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Visit not found.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating visit.");
            }
        });

        return panel;
    }


    private JPanel createDisplaySortedCountriesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        JButton displayButton = new JButton("Display Countries");

        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        panel.add(displayButton, BorderLayout.SOUTH);

        displayButton.addActionListener(e -> {
            try {
                String query = "SELECT country_name, rating FROM visits WHERE best_feature = 'Food' ORDER BY rating DESC";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                StringBuilder results = new StringBuilder();
                while (resultSet.next()) {
                    String countryName = resultSet.getString("country_name");
                    int rating = resultSet.getInt("rating");
                    results.append("Country: ").append(countryName).append(", Rating: ").append(rating).append("\n");
                }

                resultArea.setText(results.toString());
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching sorted countries.");
            }
        });

        return panel;
    }


    private JPanel createDisplayImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField visitIdField = new JTextField();
        JButton displayButton = new JButton("Display Image");
        JLabel imageLabel = new JLabel();

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Visit ID:"));
        inputPanel.add(visitIdField);
        inputPanel.add(new JPanel());
        inputPanel.add(displayButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        displayButton.addActionListener(e -> {
            int visitId = Integer.parseInt(visitIdField.getText());
            String imagePath = "images/Location" + visitId + ".jpg";

            ImageIcon imageIcon = new ImageIcon(imagePath);
            if (imageIcon.getIconWidth() == -1) {
                JOptionPane.showMessageDialog(this, "Image not found for Visit ID: " + visitId);
            } else {
                imageLabel.setIcon(imageIcon);
            }
        });

        return panel;
    }



    private JPanel createDisplayVisitsByYearPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextField yearField = new JTextField();
        JButton displayButton = new JButton("Display Visits");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);
        inputPanel.add(new JPanel());
        inputPanel.add(displayButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        displayButton.addActionListener(e -> {
            int year = Integer.parseInt(yearField.getText());

            try {
                String query = "SELECT country_name, city_name, season_visited, best_feature, comments, rating FROM visits WHERE year_visited = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, year);
                ResultSet resultSet = statement.executeQuery();

                StringBuilder results = new StringBuilder();
                while (resultSet.next()) {
                    String countryName = resultSet.getString("country_name");
                    String cityName = resultSet.getString("city_name");
                    String season = resultSet.getString("season_visited");
                    String feature = resultSet.getString("best_feature");
                    String comments = resultSet.getString("comments");
                    int rating = resultSet.getInt("rating");

                    results.append("Country: ").append(countryName)
                            .append(", City: ").append(cityName)
                            .append(", Season: ").append(season)
                            .append(", Best Feature: ").append(feature)
                            .append(", Comments: ").append(comments)
                            .append(", Rating: ").append(rating)
                            .append("\n\n");
                }

                resultArea.setText(results.toString());
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching visits for the specified year.");
            }
        });

        return panel;
    }



    private JPanel createDisplayMostVisitedCountriesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton displayButton = new JButton("Display Most Visited Countries");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        panel.add(displayButton, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        displayButton.addActionListener(e -> {
            try {
                String query = "SELECT country_name, COUNT(*) as visit_count " +
                        "FROM visits " +
                        "WHERE username = ? " +
                        "GROUP BY country_name " +
                        "ORDER BY visit_count DESC";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                StringBuilder results = new StringBuilder();
                int maxVisits = 0;
                boolean firstRow = true;

                while (resultSet.next()) {
                    String countryName = resultSet.getString("country_name");
                    int visitCount = resultSet.getInt("visit_count");

                    if (firstRow) {
                        maxVisits = visitCount;
                        firstRow = false;
                    }

                    if (visitCount == maxVisits) {
                        results.append("Country: ").append(countryName)
                                .append(", Visits: ").append(visitCount).append("\n");
                    } else {
                        break;
                    }
                }

                if (results.length() == 0) {
                    resultArea.setText("No visits found.");
                } else {
                    resultArea.setText(results.toString());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching most visited countries.");
            }
        });

        return panel;
    }



    private JPanel createDisplaySpringVisitsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton displayButton = new JButton("Display Spring Visits");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        panel.add(displayButton, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        displayButton.addActionListener(e -> {
            try {
                String query = "SELECT DISTINCT country_name " +
                        "FROM visits " +
                        "WHERE season_visited = 'spring' " +
                        "AND country_name NOT IN (" +
                        "SELECT country_name " +
                        "FROM visits " +
                        "WHERE season_visited <> 'spring')";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                StringBuilder results = new StringBuilder();
                while (resultSet.next()) {
                    String countryName = resultSet.getString("country_name");
                    results.append("Country: ").append(countryName).append("\n");
                }

                if (results.length() == 0) {
                    resultArea.setText("No countries visited only in spring.");
                } else {
                    resultArea.setText(results.toString());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching spring visits.");
            }
        });

        return panel;
    }



    private JPanel createShareVisitPanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());


        JTextField friendUsernameField = new JTextField(15);
        JTextField visitIdField = new JTextField(15);
        JButton shareButton = new JButton("Share Visit");


        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Friend's Username:"));
        inputPanel.add(friendUsernameField);
        inputPanel.add(new JLabel("Visit ID:"));
        inputPanel.add(visitIdField);
        inputPanel.add(shareButton);

        panel.add(inputPanel, BorderLayout.NORTH);


        shareButton.addActionListener(e -> {
            String friendUsername = friendUsernameField.getText();
            String visitIdStr = visitIdField.getText();

            if (friendUsername.isEmpty() || visitIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both friend's username and visit ID.");
                return;
            }

            try {
                int visitId = Integer.parseInt(visitIdStr);


                String query = "INSERT INTO sharedvisits (friend_username, visit_id, username) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, friendUsername);
                statement.setInt(2, visitId);
                statement.setString(3, username);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Visit shared successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid visit ID.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error sharing visit.");
            }
        });

        return panel;
    }



    private JPanel createSharedVisitsWithMePanel(String username) {
        JPanel panel = new JPanel(new BorderLayout());

        JButton displayButton = new JButton("Display Shared Visits");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);

        panel.add(displayButton, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        displayButton.addActionListener(e -> {
            try {
                String query = "SELECT v.country_name, v.city_name, v.season_visited, v.best_feature, v.comments, v.rating " +
                        "FROM sharedvisits s " +
                        "JOIN visits v ON s.visit_id = v.visit_id " +
                        "WHERE s.friend_username = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                StringBuilder results = new StringBuilder();
                while (resultSet.next()) {
                    String countryName = resultSet.getString("country_name");
                    String cityName = resultSet.getString("city_name");
                    String seasonVisited = resultSet.getString("season_visited");
                    String bestFeature = resultSet.getString("best_feature");
                    String comments = resultSet.getString("comments");
                    int rating = resultSet.getInt("rating");

                    results.append("Country: ").append(countryName).append("\n")
                            .append("City: ").append(cityName).append("\n")
                            .append("Season: ").append(seasonVisited).append("\n")
                            .append("Best Feature: ").append(bestFeature).append("\n")
                            .append("Comments: ").append(comments).append("\n")
                            .append("Rating: ").append(rating).append("\n")
                            .append("-------------------------------------------------\n");
                }

                if (results.length() == 0) {
                    resultArea.setText("No visits shared with you.");
                } else {
                    resultArea.setText(results.toString());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching shared visits.");
            }
        });

        return panel;
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String username = "user1";
            new MainFrame(username);
        });
    }
}
