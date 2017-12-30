package photomanager.Controllers;

import org.junit.Test;
import photomanager.Models.ImageModel;
import photomanager.Models.TagModel;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ImageControllerTest {

    private ImageController imageController = new ImageController();

    @Test
    public void findImage() throws Exception {

        ImageModel car = new ImageModel("car.jpg", "/User1/Folder1/car.jpg");
        ImageModel bus = new ImageModel("bus.jpg", "/User2/Folder2/bus.jpg");
        ImageModel boat = new ImageModel("boat.jpg", "/User3/Folder3/car.jpg");

        ImageController.getImageCollection().add(car);
        ImageController.getImageCollection().add(bus);
        ImageController.getImageCollection().add(boat);

        String imageLocation = bus.getImageLocation();
        ImageModel myImage = imageController.findImage(imageLocation);
        assertEquals(myImage, bus);
    }

    @Test
    public void findNonExistentImage() throws Exception {

        ImageModel car = new ImageModel("car.jpg", "/User1/Folder1/car.jpg");
        ImageModel bus = new ImageModel("bus.jpg", "/User2/Folder2/bus.jpg");
        ImageModel boat = new ImageModel("boat.jpg", "/User3/Folder3/car.jpg");

        ImageController.getImageCollection().add(car);
        ImageController.getImageCollection().add(bus);
        ImageController.getImageCollection().add(boat);

        ImageModel myImage = imageController.findImage("/User4/Folder4/airplane.jpg");
        ImageModel expectedImage = new ImageModel("","");
        assertEquals(myImage, expectedImage);
    }

    @Test
    public void move() throws Exception {
        ImageModel car = new ImageModel("car.jpg", "/User1/Folder1/car.jpg");
        imageController.move(car, "/User1/Folder2");
        String expectedLocation = "/User1/Folder2/car.jpg";
        assertEquals(expectedLocation, car.getImageLocation());
    }

    @Test
    public void moveToLocationContainingSpace() throws Exception {
        ImageModel car = new ImageModel("car.jpg", "/User1/Folder 1/car.jpg");
        imageController.move(car, "/User1/Folder 2");
        String expectedLocation = "/User1/Folder 2/car.jpg";
        assertEquals(expectedLocation, car.getImageLocation());
    }

    @Test
    public void parseTags() throws Exception {
        ArrayList<TagModel> expectedTags = new ArrayList<>();
        expectedTags.add(new TagModel("tag1"));
        expectedTags.add(new TagModel("t2"));
        expectedTags.add(new TagModel("myTag3"));

        ImageModel car =
                new ImageModel("car @tag1 @t2 @myTag3.jpg", "/User1/Folder1/car @tag1 @t2 @myTag3.jpg");
        ArrayList<TagModel> parsedTags = ImageController.parseTags(car);
        assertEquals(expectedTags, parsedTags);
    }

    @Test
    public void parseEmptyImageTags() throws Exception {
        ImageModel car = new ImageModel("car.jpg", "/User1/Folder1/car.jpg");
        ArrayList<TagModel> parsedTags = ImageController.parseTags(car);
        assertTrue(parsedTags.isEmpty());
    }

    @Test
    public void emptyTrash() throws Exception {
        ImageModel car = new ImageModel("car.jpg", "/User1/Folder1/car.jpg");
        ImageModel bus = new ImageModel("bus.jpg", "/User2/Folder2/bus.jpg");
        ImageModel boat = new ImageModel("boat.jpg", "/User3/Folder3/car.jpg");

        ImageController.getItemsInTrash().add(car);
        ImageController.getItemsInTrash().add(bus);
        ImageController.getItemsInTrash().add(boat);

        imageController.emptyTrash();

        assertTrue(ImageController.getItemsInTrash().isEmpty());
    }

}