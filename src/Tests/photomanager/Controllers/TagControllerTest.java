package photomanager.Controllers;

import org.junit.Test;
import photomanager.Models.ImageModel;
import photomanager.Models.TagModel;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TagControllerTest {
    private TagController tagController = new TagController();

    @Test
    public void addSingleTag() throws Exception {
        ImageModel car = new ImageModel("car.jpg", "/User1/Folder1/car.jpg");
        tagController.addTag(new TagModel("tag1"), car);
        ImageModel expectedImage = new ImageModel("car @tag1.jpg","/User1/Folder1/car @tag1.jpg");
        assertEquals(expectedImage, car);
    }

    @Test
    public void addMultipleTags() throws Exception {
        ImageModel car = new ImageModel("car.jpg", "/User1/Folder1/car.jpg");
        tagController.addTag(new TagModel("tag1"), car);
        tagController.addTag(new TagModel("t2"), car);
        ImageModel expectedImage = new ImageModel("car @tag1 @t2.jpg","/User1/Folder1/car @tag1 @t2.jpg");
        assertEquals(expectedImage, car);
    }

    @Test
    public void containsInTagCollection() throws Exception {
        TagController.getTagCollection().add(new TagModel("tag1"));
        TagController.getTagCollection().add(new TagModel("t2"));
        assertTrue(tagController.containsInTagCollection(new TagModel("t2")));
    }

    @Test
    public void notContainsInTagCollection() throws Exception {
        TagController.getTagCollection().add(new TagModel("tag1"));
        TagController.getTagCollection().add(new TagModel("t2"));
        assertTrue(!tagController.containsInTagCollection(new TagModel("myTag3")));
    }

    @Test
    public void deleteOneTag() throws Exception {
        ImageModel car = new ImageModel("car @tag1 @t2.jpg","/User1/Folder1/car @tag1 @t2.jpg");
        tagController.deleteTag(new TagModel("tag1"), car);
        ImageModel expectedImage = new ImageModel("car @t2.jpg", "/User1/Folder1/car @t2.jpg");
        assertEquals(expectedImage, car);
    }

    @Test
    public void deleteMultipleTags() throws Exception {
        ImageModel car = new ImageModel("car @tag1 @t2 @t3.jpg","/User1/Folder1/car @tag1 @t2 @t3.jpg");
        tagController.deleteTag(new TagModel("tag1"), car);
        tagController.deleteTag(new TagModel("t3"), car);
        ImageModel expectedImage = new ImageModel("car @t2.jpg", "/User1/Folder1/car @t2.jpg");
        assertEquals(expectedImage, car);
    }

    @Test
    public void deleteNonExistentTag() throws Exception {
        ImageModel car = new ImageModel("car @tag1 @t2.jpg","/User1/Folder1/car @tag1 @t2.jpg");
        tagController.deleteTag(new TagModel("t3"), car);
        ImageModel expectedImage = new ImageModel("car @tag1 @t2.jpg","/User1/Folder1/car @tag1 @t2.jpg");
        assertEquals(expectedImage, car);
    }

    @Test
    public void revertTag() throws Exception {
        ImageModel car = new ImageModel("car @tag1 @t2 @t3.jpg","/User1/Folder1/car @tag1 @t2 @t3.jpg");
        ArrayList<TagModel> tagListToRevert = new ArrayList<>();
        tagListToRevert.add(new TagModel("tag1"));
        tagListToRevert.add(new TagModel("t3"));
        tagController.revertTag(car, tagListToRevert);
        ImageModel expectedImage = new ImageModel("car @tag1 @t3.jpg","/User1/Folder1/car @tag1 @t3.jpg");
        assertEquals(expectedImage, car);
    }
}