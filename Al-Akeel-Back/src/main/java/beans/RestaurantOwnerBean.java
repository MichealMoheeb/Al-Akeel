package beans;

import entities.AkeelOrder;
import entities.Meal;
import entities.Restaurant;
import entities.User;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.persistence.*;
import jakarta.ws.rs.*;

import java.util.List;

@Stateless
@Path("/restaurant")
@Produces("application/json")
@Consumes("application/json")
public class RestaurantOwnerBean {

    @PersistenceContext
    EntityManager em;

    private User owner = null;
    private Restaurant restaurant = null;

    // login providing username and password in a json object
    @POST
    @Path("/login")
    public User login(User owner) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.username='" + owner.getUsername() + "' AND u.password='" + owner.getPassword() + "'");
        if (query.getResultList().isEmpty())
            return null;
        else {
            User loggedInUser = (User) query.getSingleResult();
            if (loggedInUser.getRole().equals(User.UserRole.RESTAURANT_OWNER)){
                this.owner = loggedInUser;
                return loggedInUser;
            }
            else
                return null;
        }
    }

    // signup providing username, password and name in a json object
    @POST
    @Path("/signup")
    public Object signup(User owner) {
        owner.setRole(User.UserRole.RESTAURANT_OWNER);
        em.persist(owner);
        return owner;
    }


    // create a restaurant entity in the db by providing the name in a json object
    @POST
    @Path("/create")
    public Object createRestaurant(Restaurant restaurant) {
        if (this.restaurant != null) {
            return Json.createObjectBuilder().add("message", "restaurant was created previously").build();
        }
        restaurant.setOwnerId(owner.getId());
        em.persist(restaurant);
        this.restaurant = restaurant;
        return restaurant;
    }

    // create menu of the previously created restaurant by providing an array of meals in json
    @POST
    @Path("/menu")
    public Object createRestaurantMenu(List<Meal> meals) {
        if (this.restaurant == null) {
            return Json.createObjectBuilder().add("message", "restaurant not created").build();
        }
        if (this.restaurant.getMeals() != null) {
            return Json.createObjectBuilder().add("message", "menu was created previously").build();
        }
        for (Meal meal:meals) {
            meal.setRestaurant(this.restaurant);
            em.persist(meal);
        }
        this.restaurant.setMeals(meals);
        em.merge(this.restaurant);
        return Json.createObjectBuilder().add("message", "menu created successfully").build();
    }

    // edit restaurant menu by giving an array of meals that replace the original meals
    @PUT
    @Path("/edit")
    public Object editRestaurant(List<Meal> meals) {
        if (this.restaurant == null) {
            return Json.createObjectBuilder().add("message", "restaurant not created").build();
        }
        for (Meal meal: this.restaurant.getMeals()) {
            meal.setRestaurant(null);
            em.merge(meal);
        }
        this.restaurant.setMeals(null);
        em.merge(this.restaurant);
        if(meals != null) {
            for (Meal meal : meals) {
                meal.setRestaurant(this.restaurant);
                em.merge(meal);
            }
            this.restaurant.setMeals(meals);
        } else {
            return Json.createObjectBuilder().add("message", "You should create a menu first").build();
        }
        em.merge(this.restaurant);
        return Json.createObjectBuilder().add("message", "Restaurant edited successfully").build();
    }

    // get a restaurant by id
    @GET
    @Path("/get-restaurant/{id}")
    public Restaurant getRestaurantById(@PathParam("id") int id) {
        Query query = em.createQuery("SELECT c FROM Restaurant c JOIN FETCH c.meals WHERE c.id=" + id);
        if (query.getResultList().isEmpty())
            return null;
        return (Restaurant) query.getSingleResult();
    }

    // generate a report for the restaurant of the given id
    @GET
    @Path("/report/{id}")
    public Object createReport(@PathParam("id") int id) {
        Query query = em.createQuery("SELECT c FROM AkeelOrder c WHERE c.restaurant=" + id);
        if (query.getResultList().isEmpty()) {
            return "Restaurant does not exist";
        }
        List<AkeelOrder> orders = query.getResultList();
        Query query1 = em.createQuery("SELECT c FROM Restaurant c WHERE c.id=" + id);
        Restaurant restaurant = (Restaurant) query1.getSingleResult();
        String restaurantName = restaurant.getName();

        double sum = 0;
        int completed = 0;
        int canceled = 0;
        for (int i=0; i<orders.size(); i++) {
            if (orders.get(i).getOrderStatus().equals(AkeelOrder.OrderStatus.DELIVERED)) {
                sum += orders.get(i).getTotalPrice();
                completed += 1;
            }
            else if (orders.get(i).getOrderStatus().equals(AkeelOrder.OrderStatus.CANCELED)) {
                canceled += 1;
            }
        }
        return Json.createObjectBuilder()
                .add("Restaurant", restaurantName)
                .add("Total earnings", sum)
                .add("Completed orders", completed)
                .add("Canceled orders", canceled)
                .build();
    }

    @PUT
    @Path("/cancel-order/{id}")
    public Object cancelOrder(@PathParam("id") long id) {
        AkeelOrder order = em.find(AkeelOrder.class, id);
        order.setOrderStatus(AkeelOrder.OrderStatus.CANCELED);
        em.merge(order);
        return Json.createObjectBuilder().add("message", "Order cancelled successfully").build();
    }
}