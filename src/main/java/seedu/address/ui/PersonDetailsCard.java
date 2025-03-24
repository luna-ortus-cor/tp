package seedu.address.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.attribute.Attribute;
import seedu.address.model.person.Person;

/**
 * An UI component that displays detailed information of a {@code Person}.
 */
public class PersonDetailsCard extends UiPart<Region> {

    private static final String FXML = "PersonDetailsCard.fxml";

    public final Person person;

    @FXML
    private VBox detailsPane;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane attributes;

    /**
     * Creates a {@code PersonDetailsCard} with the given {@code Person}.
     */
    public PersonDetailsCard(Person person) {
        super(FXML);
        this.person = person;
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        person.getAttributes().stream()
                .sorted(Comparator.comparing(Attribute::getAttributeName))
                .forEach(attribute -> attributes.getChildren().add(createAttributeLabel(attribute)));
    }

    private static Label createAttributeLabel(Attribute attribute) {
        Label label = new Label(attribute.getDisplayText());
        if (attribute.hasSiteLink()) {
            label.setId("site-attribute-details");
            label.setOnMouseClicked(event -> {
                String link = attribute.getSiteLink().orElseThrow(() ->
                        new AssertionError("Unreachable, Optional<String> siteLink should not be empty"))
                        + attribute.getAttributeValue();
                try {
                    Desktop.getDesktop().browse(new URI(link));
                } catch (IOException | URISyntaxException e) {
                    // Solution below inspired by
                    // https://stackoverflow.com/questions/45620901/javafx-copy-text-from-alert
                    TextArea textArea = new TextArea("There was an error in opening the link: " + link);
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    GridPane gridPane = new GridPane();
                    gridPane.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(textArea, 0, 0);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.getDialogPane().setContent(gridPane);
                    alert.showAndWait();
                }
            });
        }
        return label;
    }
}
