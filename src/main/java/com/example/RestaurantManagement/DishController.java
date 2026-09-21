package com.example.RestaurantManagement;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class DishController {
    private CartController cartController;

    // === Αναφορά στα ImageView των πιάτων (BESTSELLER) από το FXML ===
    @FXML private ImageView GarlicButterFriesBESTSELLER;
    @FXML private ImageView FreshAvocadoBESTSELLER;
    @FXML private ImageView SpiceyAvocadoNachosBESTSELLER;
    @FXML private ImageView MediterraneanMezzePlatterBESTSELLER;
    @FXML private ImageView KoreanFriedCauliflowerwithEdamameBESTSELLER;
    @FXML private ImageView BuffaloChickenFrieswithColeslawBESTSELLER;
    @FXML private ImageView SpicyChorizoBitesBESTSELLER;
    @FXML private ImageView SteakPotatoAlfredoPizzaBESTSELLER;
    @FXML private ImageView TwistedReubenFriesBESTSELLER;
    @FXML private ImageView AuthenticGreekSkordaliaBESTSELLER;
    @FXML private ImageView FlavorfulVeggieTacosBESTSELLER;
    @FXML private ImageView BanhMiFriesBESTSELLER;
    @FXML private ImageView CheddarBeefOmeletteBESTSELLER;
    @FXML private ImageView ChocolateProteinPancakesBESTSELLER;
    @FXML private ImageView ProteinFrenchToastBESTSELLER;
    @FXML private ImageView BerryProteinParfaitBESTSELLER;
    @FXML private ImageView CrispyProteinWafflesBESTSELLER;
    @FXML private ImageView BlueberryProteinMuffinsBESTSELLER;
    @FXML private ImageView ChorizoPotatoHashBESTSELLER;
    @FXML private ImageView ProteinBananaPancakesBESTSELLER;
    @FXML private ImageView AvocadoEggToastBESTSELLER;
    @FXML private ImageView GreekYogurtProteinPancakesBESTSELLER;
    @FXML private ImageView ProteinCheesecakePancakesBESTSELLER;
    @FXML private ImageView RaspberryWhiteChocolateMuffinsBESTSELLER;
    @FXML private ImageView ButterChickenBESTSELLER;
    @FXML private ImageView CajunChickenPastaBESTSELLER;
    @FXML private ImageView ClassicCarbonaraBESTSELLER;
    @FXML private ImageView ChickenCalzoneBESTSELLER;
    @FXML private ImageView CreamyBeefMacCheeseBESTSELLER;
    @FXML private ImageView ChickenTikkaMasalawithPilauRiceBESTSELLER;
    @FXML private ImageView SpicyNandosChickenwithGoldenRiceBESTSELLER;
    @FXML private ImageView SmashBurgerCrinkleFriesBESTSELLER;
    @FXML private ImageView CrispyOrangeChickenwithBasmatiRiceBESTSELLER;
    @FXML private ImageView CrispyBangBangChickenBESTSELLER;
    @FXML private ImageView CrispyChickenEggFriedRiceBESTSELLER;
    @FXML private ImageView TandooriKebabswithRefreshingMintYogurtBESTSELLER;

    @FXML
    private Label cartItemCountLabel;



    // === Χάρτης που αντιστοιχεί τα ονόματα των πιάτων στα αντίστοιχα ImageView ===
    private Map<String, ImageView> dishImageMap = new HashMap<>();

    // Μέθοδος για την αρχικοποίηση του dishImageMap
    private void setupDishImageMap() {
        if (GarlicButterFriesBESTSELLER != null) {
            dishImageMap.put("Garlic Butter Fries with Parmesan", GarlicButterFriesBESTSELLER);
        }
        if (FreshAvocadoBESTSELLER != null) {
            dishImageMap.put("Fresh Avocado Apple & Quinoa Salad", FreshAvocadoBESTSELLER);
        }
        if (SpiceyAvocadoNachosBESTSELLER != null) {
            dishImageMap.put("Spicy Avocado Nachos", SpiceyAvocadoNachosBESTSELLER);
        }
        if (MediterraneanMezzePlatterBESTSELLER != null) {
            dishImageMap.put("Mediterranean Mezze Platter", MediterraneanMezzePlatterBESTSELLER);
        }
        if (KoreanFriedCauliflowerwithEdamameBESTSELLER != null) {
            dishImageMap.put("Korean Fried Cauliflower with Edamame", KoreanFriedCauliflowerwithEdamameBESTSELLER);
        }
        if (BuffaloChickenFrieswithColeslawBESTSELLER != null) {
            dishImageMap.put("Buffalo Chicken Fries with Coleslaw", BuffaloChickenFrieswithColeslawBESTSELLER);
        }
        if (SpicyChorizoBitesBESTSELLER != null) {
            dishImageMap.put("Spicy Chorizo Bites", SpicyChorizoBitesBESTSELLER);
        }
        if (SteakPotatoAlfredoPizzaBESTSELLER != null) {
            dishImageMap.put("Steak & Potato Alfredo Pizza", SteakPotatoAlfredoPizzaBESTSELLER);
        }
        if (TwistedReubenFriesBESTSELLER != null) {
            dishImageMap.put("Twisted Reuben Fries", TwistedReubenFriesBESTSELLER);
        }
        if (AuthenticGreekSkordaliaBESTSELLER != null) {
            dishImageMap.put("Authentic Greek Skordalia", AuthenticGreekSkordaliaBESTSELLER);
        }
        if (FlavorfulVeggieTacosBESTSELLER != null) {
            dishImageMap.put("Flavorful Veggie Tacos", FlavorfulVeggieTacosBESTSELLER);
        }
        if (BanhMiFriesBESTSELLER != null) {
            dishImageMap.put("Banh Mi Fries", BanhMiFriesBESTSELLER);
        }
        if (CheddarBeefOmeletteBESTSELLER != null) {
            dishImageMap.put("Cheddar & Beef Omelette", CheddarBeefOmeletteBESTSELLER);
        }
        if (ChocolateProteinPancakesBESTSELLER != null) {
            dishImageMap.put("Chocolate Protein Pancakes", ChocolateProteinPancakesBESTSELLER);
        }
        if (ProteinFrenchToastBESTSELLER != null) {
            dishImageMap.put("Protein French Toast", ProteinFrenchToastBESTSELLER);
        }
        if (BerryProteinParfaitBESTSELLER != null) {
            dishImageMap.put("Berry Protein Parfait", BerryProteinParfaitBESTSELLER);
        }
        if (CrispyProteinWafflesBESTSELLER != null) {
            dishImageMap.put("Crispy Protein Waffles", CrispyProteinWafflesBESTSELLER);
        }
        if (BlueberryProteinMuffinsBESTSELLER != null) {
            dishImageMap.put("Blueberry Protein Muffins", BlueberryProteinMuffinsBESTSELLER);
        }
        if (ChorizoPotatoHashBESTSELLER != null) {
            dishImageMap.put("Chorizo & Potato Hash", ChorizoPotatoHashBESTSELLER);
        }
        if (ProteinBananaPancakesBESTSELLER != null) {
            dishImageMap.put("Protein Banana Pancakes", ProteinBananaPancakesBESTSELLER);
        }
        if (AvocadoEggToastBESTSELLER != null) {
            dishImageMap.put("Avocado & Egg Toast", AvocadoEggToastBESTSELLER);
        }
        if (GreekYogurtProteinPancakesBESTSELLER != null) {
            dishImageMap.put("Greek Yogurt Protein Pancakes", GreekYogurtProteinPancakesBESTSELLER);
        }
        if (ProteinCheesecakePancakesBESTSELLER != null) {
            dishImageMap.put("Protein Cheesecake Pancakes", ProteinCheesecakePancakesBESTSELLER);
        }
        if (RaspberryWhiteChocolateMuffinsBESTSELLER != null) {
            dishImageMap.put("Raspberry & White Chocolate Muffins", RaspberryWhiteChocolateMuffinsBESTSELLER);
        }
        if (ButterChickenBESTSELLER != null) {
            dishImageMap.put("Butter Chicken", ButterChickenBESTSELLER);
        }
        if (CajunChickenPastaBESTSELLER != null) {
            dishImageMap.put("Cajun Chicken Pasta", CajunChickenPastaBESTSELLER);
        }
        if (ClassicCarbonaraBESTSELLER != null) {
            dishImageMap.put("Classic Carbonara", ClassicCarbonaraBESTSELLER);
        }
        if (ChickenCalzoneBESTSELLER != null) {
            dishImageMap.put("Chicken Calzone", ChickenCalzoneBESTSELLER);
        }
        if (CreamyBeefMacCheeseBESTSELLER != null) {
            dishImageMap.put("Creamy Beef Mac & Cheese", CreamyBeefMacCheeseBESTSELLER);
        }
        if (ChickenTikkaMasalawithPilauRiceBESTSELLER != null) {
            dishImageMap.put("Chicken Tikka Masala with Pilau Rice", ChickenTikkaMasalawithPilauRiceBESTSELLER);
        }
        if (SpicyNandosChickenwithGoldenRiceBESTSELLER != null) {
            dishImageMap.put("Spicy Nando's Chicken with Golden Rice", SpicyNandosChickenwithGoldenRiceBESTSELLER);
        }
        if (SmashBurgerCrinkleFriesBESTSELLER != null) {
            dishImageMap.put("Smash Burger with Crinkle Fries", SmashBurgerCrinkleFriesBESTSELLER);
        }
        if (CrispyOrangeChickenwithBasmatiRiceBESTSELLER != null) {
            dishImageMap.put("Crispy Orange Chicken with Basmati Rice", CrispyOrangeChickenwithBasmatiRiceBESTSELLER);
        }
        if (CrispyBangBangChickenBESTSELLER != null) {
            dishImageMap.put("Crispy Bang Bang Chicken", CrispyBangBangChickenBESTSELLER);
        }
        if (CrispyChickenEggFriedRiceBESTSELLER != null) {
            dishImageMap.put("Crispy Chicken Egg Fried Rice", CrispyChickenEggFriedRiceBESTSELLER);
        }
        if (TandooriKebabswithRefreshingMintYogurtBESTSELLER != null) {
            dishImageMap.put("Tandoori Kebabs with Refreshing Mint Yogurt", TandooriKebabswithRefreshingMintYogurtBESTSELLER);
        }
    }

    // === Χάρτης που κρατά τα σύνολα πωλήσεων ανά πιάτο ===
    private Map<String, Integer> dishSales = new HashMap<>();


    // === Φορτώνει τα δεδομένα πωλήσεων από CSV αρχείο ===
    public void loadDishData() {
        File file = new File("orders/dish_sales.csv");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isHeader = true;

                while ((line = reader.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }

                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        String dishName = parts[0].trim();
                        String salesCountStr = parts[1].trim();

                        try {
                            int salesCount = Integer.parseInt(salesCountStr);
                            dishSales.put(dishName, salesCount);
                        } catch (NumberFormatException e) {
                            System.out.println("Μη έγκυρος αριθμός πωλήσεων για το πιάτο: " + dishName + " - Τιμή πωλήσεων: " + salesCountStr);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Αρχείο δεν βρέθηκε!");
        }
    }

    // === Επιστρέφει τα 5 πιο δημοφιλή πιάτα με βάση τις πωλήσεις ===
    public List<String> getTopSellingDishes(int limit) {
        return dishSales.entrySet().stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue())) // Ταξινόμηση με βάση τις πωλήσεις
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // === Εμφανίζει μόνο τα top 5 πιάτα, κρύβοντας τα υπόλοιπα ===
    public void updateTopDishesVisibility() {
        List<String> topDishes = getTopSellingDishes(5);

        for (ImageView imageView : dishImageMap.values()) {
            imageView.setVisible(false);
        }

        for (String dishName : topDishes) {
            ImageView imageView = dishImageMap.get(dishName);
            if (imageView != null) {
                imageView.setVisible(true);
            }
        }
    }

    // === Setter για να περάσουμε CartController ===
    public void setCartController(CartController cartController) {
        this.cartController = cartController;
    }

    // === Εναλλαγή Σκηνών ===
    @FXML private void BreakfastOrder(javafx.event.ActionEvent event) throws IOException {
        loadScene("Breakfast_Order.fxml", event);
    }

    @FXML private void MainCourseOrder(javafx.event.ActionEvent event) throws IOException {
        loadScene("MainCourse_Order.fxml", event);
    }

    @FXML private void AppetizersOrder(javafx.event.ActionEvent event) throws IOException {
        loadScene("Appetizers_Order.fxml", event);
    }
    @FXML private void BeveragesOrder(javafx.event.ActionEvent event) throws IOException {
        loadScene("Beverages_Order.fxml", event);
    }
    @FXML private void Cart(javafx.event.ActionEvent event) throws IOException {
        loadScene("cart.fxml", event);
    }
    @FXML private void HomePage(javafx.event.ActionEvent event) throws IOException {
        loadScene("HomePage.fxml", event);
    }

    // === Μέθοδος για φόρτωση FXML σκηνής και αλλαγή παραθύρου ===
    private void loadScene(String fxmlFile, javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Ορισμός των VBox για εμφάνιση επιλογών πιάτων και σχολίων στο UI για τα ποτά
    @FXML private VBox dishOptionsPane1;
    @FXML private VBox dishOptionsPane2;

    @FXML private VBox dishCommentPane1;
    @FXML private VBox dishCommentPane2;
    @FXML private VBox dishCommentPane3;
    @FXML private VBox dishCommentPane4;
    @FXML private VBox dishCommentPane5;
    @FXML private VBox dishCommentPane6;

    // Map που αποθηκεύει τα πιάτα με κλειδί το όνομα και τιμή το αντικείμενο Dish
    private Map<String, Dish> dishes = new HashMap<>();

    // Μέθοδος initialize που εκτελείται κατά την αρχικοποίηση του controller
    public void initialize() {

        // Προσθήκη πιάτου στο Map dishes — κάθε πιάτο έχει όνομα, περιγραφή, εικόνα, λίστα υλικών, τιμή και κατηγορία
        dishes.put("Cheddar & Beef Omelette", new Dish(
                "Cheddar & Beef Omelette",
                "Ομελέτα με μοσχαρίσιο κιμά, καραμελωμένο κρεμμύδι, τσένταρ & μυρωδικά.",
                "image/Cheesy_BEEF_BULking_omelette.png",
                List.of("Μανιτάρια", "Μοσχαρίσιος κιμάς", "Πιπεριές πράσινες",
                        "Μπέικον", "Πατάτα σε μικρούς κύβους","Ντομάτα"),
        8.50,
                "Food"
        ));


        dishes.put("Chorizo & Potato Hash", new Dish(
                "Chorizo & Potato Hash",
                "Πατάτες με chorizo, καραμελωμένο κρεμμύδι, αυγό μάτι & καπνιστή πάπρικα.",
                "image/Spicy_chorizo_breakfast_hash.JPG",
                List.of("Μανιτάρια ", "Κρεμμύδι", "Πιπεριές κόκκινες", "Πιπεριές πράσινες", "Φρέσκες ντομάτες"),
                11,
                "Food"
        ));

        dishes.put("Protein Banana Pancakes", new Dish(
                "Protein Banana Pancakes",
                "Pancakes βρώμης με μπανάνα, σοκολάτα, κανέλα & σιρόπι σφενδάμου.",
                "image/Protein_Banana_Pancakes.JPG",
                List.of("Φυστικοβούτυρο", "Σπόροι chia", "Κανέλα", "Τριμμένη καρύδα"),
                8.90,
                "Food"
        ));

        dishes.put("Chocolate Protein Pancakes", new Dish(
                "Chocolate Protein Pancakes",
                "Pancakes σοκολάτας με πρωτεΐνη, κανέλα, fudge κακάο & σιρόπι σφενδάμου.",
                "image/Chocolate_Protein_Pancakes.JPG",
                List.of("Μπανάνα", "Φυστικοβούτυρο", "Σκόνη κακάο", "Τριμμένη μαύρη σοκολάτα", "κανέλα"),
                10.60,
                "Food"
        ));

        dishes.put("Protein French Toast", new Dish(
                "Protein French Toast",
                "French toast ολικής με βανίλια, κανέλα, μοσχοκάρυδο, blueberries & σιρόπι σφενδάμου.",
                "image/Protein_French_Toast.JPG",
                List.of("Μούρα", "Μπανάνα", "Maple syrup", "Τριμμένη μαύρη σοκολάτα", "κανέλα"),
                9,
                "Food"
        ));

        dishes.put("Berry Protein Parfait", new Dish(
                "Berry Protein Parfait",
                "Γιαούρτι με βανίλια, granola, δασικά μούρα & σιρόπι σφενδάμου.",
                "image/Berry_Protein_Parfait.JPG",
                List.of("Φρέσκα berries", "Τριμμένοι ξηροί καρποί", "Φυστικοβούτυρο", "Μέλι"),
                8.60,
                "Food"
        ));

        dishes.put("Avocado & Egg Toast", new Dish(
                "Avocado & Egg Toast",
                "Ψωμί ολικής με αβοκάντο, αυγά, κόκκινο κρεμμύδι & αρωματικά.",
                "image/Avocado_and_Egg_Toast.JPG",
                List.of("Σουσάμι", "Τριμμένο τυρί", "Τραγανό μπέικον", "μπούκοβο"),
                11.50,
                "Food"
        ));

        dishes.put("Greek Yogurt Protein Pancakes", new Dish(
                "Greek Yogurt Protein Pancakes",
                "Pancakes με γιαούρτι, βανίλια & σιρόπι χωρίς θερμίδες ή χυμό λεμονιού.",
                "image/Greek_Yogurt_Protein_Pancakes.JPG",
                List.of("Φρούτα του δάσους", "Φυστικοβούτυρο", "Μέλι", "Τριμμένη μαύρη σοκολάτα","Καρύδα"),
                9.50,
                "Food"
        ));

        dishes.put("Protein Cheesecake Pancakes", new Dish(
                "Protein Cheesecake Pancakes",
                "Pancakes βρώμης με γέμιση cheesecake, μούρα & σιρόπι χωρίς θερμίδες.",
                "image/Protein_Cheesecake_Pancakes.JPG",
                List.of("Φράουλες", "Τριμμένο digestive", "chips μαύρης σοκολάτας", "Κανέλα"),
                9,
                "Food"
        ));

        dishes.put("Crispy Protein Waffles", new Dish(
                "Crispy Protein Waffles",
                "Βάφλες με βανίλια & πρωτεΐνη, φράουλες & σιρόπι χωρίς θερμίδες.",
                "image/Crispy_Protein_Waffles.JPG",
                List.of("Μούρα", "Σοκολάτα υγείας", "granola", "Σκόνη κακάο"),
                12,
                "Food"
        ));

        dishes.put("Blueberry Protein Muffins", new Dish(
                "Blueberry Protein Muffins",
                "Muffins με μπανάνα, πρωτεΐνη βανίλιας, cottage cheese & blueberries.",
                "image/Blueberry_Protein_Muffins.JPG",
                List.of("Τριμμένο αμύγδαλο", "Ζάχαρη καρύδας", "Ξύσμα λεμονιού", "Flakes καρύδας"),
                11,
                "Food"
        ));

        dishes.put("Raspberry & White Chocolate Muffins", new Dish(
                "Raspberry & White Chocolate Muffins",
                "Muffins με μπανάνα, cottage cheese, raspberries & λευκή σοκολάτα.",
                "image/Raspberry_and_White_Chocolate_Muffins.JPG",
                List.of("Λευκή σοκολάτα", "Flakes καρύδας", "raspberry juice"),
                6,
                "Food"
        ));

        dishes.put("Butter Chicken", new Dish(
                "Butter Chicken",
                "Κοτόπουλο με σάλτσα γιαουρτιού, καρυδιού & ντομάτας, σερβιρισμένο με μπασμάτι & κόλιανδρο.",
                "image/Butter_Chicken.JPG",
                List.of("Τριμμένο κάσιους", "Ροδέλες πράσινης πιπεριάς", "Σουσάμι"),
                12.10,
                "Food"
        ));

        dishes.put("Cajun Chicken Pasta", new Dish(
                "Cajun Chicken Pasta",
                "Κοτόπουλο με καγιέν, σάλτσα ντομάτας, κρέμα τυρί & μοτσαρέλα, με πένες, σπανάκι & πιπεριές.",
                "image/Cajun_Chicken_Pasta.JPG",
                List.of("Τριμμένη παρμεζάνα", "Λωρίδες crispy bacon", "chili flakes"),
                11.30,
                "Food"
        ));

        dishes.put("Classic Carbonara", new Dish(
                "Classic Carbonara",
                "Πένες καρμπονάρα με αυγά, κρέμα, παρμεζάνα & τραγανό μπέικον.",
                "image/Classic_Carbonara.JPG",
                List.of("Φρέσκο τριμμένο πιπέρι", "Επιπλέον τριμμένο pecorino", "Crispy κομμάτια από μπέικον","Ψητό αυγό μελάτο"),
                10.20,
                "Food"
        ));

        dishes.put("Chicken Calzone", new Dish(
                "Chicken Calzone",
                "Γιαούρτι με βανίλια, granola, δασικά μούρα & σιρόπι σφενδάμου.",
                "image/Chicken_Calzone.JPG",
                List.of("Φρέσκια ρίγανη", "πεκορίνο", "chili flakes"),
                5,
                "Food"
        ));

        dishes.put("Creamy Beef Mac & Cheese", new Dish(
                "Creamy Beef Mac & Cheese",
                "Μακαρόνια με κιμά, μανιτάρια, παδρόν πιπεριές & κρεμώδη σάλτσα τυριού με sour cream.",
                "image/Creamy_Beef_Mac_and_Cheese.JPG",
                List.of("truffle oil", "Ροδέλες jalapeño", "Καραμελωμένα κρεμμύδια","Τριμμένη παρμεζάνα"),
                12,
                "Food"
        ));

        dishes.put("Chicken Tikka Masala with Pilau Rice", new Dish(
                "Chicken Tikka Masala with Pilau Rice",
                "Κοτόπουλο με σάλτσα ντομάτας & καρύδας, pilau ρύζι.",
                "image/Chicken_Tikka_Masala_with_Pilau_Rice.JPG",
                List.of("Φρέσκος κόλιανδρος", "Chili oil", "Naan","Φρυγανισμένοι ξηροί καρποί"),
                11.50,
                "Food"
        ));

        dishes.put("Spicy Nando’s Chicken with Golden Rice", new Dish(
                "Spicy Nando’s Chicken with Golden Rice",
                "Κοτόπουλο με καπνιστά μπαχαρικά & μπασμάτι με κουρκουμά.",
                "image/Spicy_Nando’s_Chicken_with_Golden_Rice.JPG",
                List.of("peri-peri sauce", "τυρί φέτα", "Ψιλοκομμένο κόκκινο κρεμμύδι","Καλαμπόκι"),
                12.90,
                "Food"
        ));

        dishes.put("Crispy Orange Chicken with Basmati Rice", new Dish(
                "Crispy Orange Chicken with Basmati Rice",
                "Κοτόπουλο με σάλτσα πορτοκαλιού, σόγιας & μελιού, σερβιρισμένο με μπασμάτι ρύζι.",
                "image/Crispy_Orange_Chicken_with_Basmati_Rice.JPG",
                List.of("Σουσάμι", "Φρέσκος κόλιανδρος", "Ξύσμα πορτοκαλιού","Crispy noodles"),
                10.50,
                "Food"
        ));

        dishes.put("Smash Burger & Crinkle Fries", new Dish(
                "Smash Burger & Crinkle Fries",
                "Smash patty με τυρί, μπέικον, μαρούλι & μαγιονέζα σε μπριός, με πατάτες & sriracha mayo.",
                "image/Smash_Burger_and_Crinkle_Fries.JPG",
                List.of("chili flakes", "πίκλες", "Pulled pork","Πικάντικη mayo"),
                12.30,
                "Food"
        ));

        dishes.put("Crispy Bang Bang Chicken", new Dish(
                "Crispy Bang Bang Chicken",
                "Smash patty με τυρί, μπέικον, μαρούλι & μαγιονέζα σε μπριός, με πατάτες & sriracha mayo.",
                "image/Crispy_Bang_Bang_Chicken.JPG",
                List.of("chili flakes", "sriracha", "Pickled onions","Τραγανό κρεμμύδι"),
                11.50,
                "Food"
        ));

        dishes.put("Tandoori Kebabs with Refreshing Mint Yogurt", new Dish(
                "Tandoori Kebabs with Refreshing Mint Yogurt",
                "Smash patty με τυρί, μπέικον, μαρούλι & μαγιονέζα σε μπριός, με πατάτες & sriracha mayo.",
                "image/Tandoori_Kebabs_with_Refreshing_Mint_Yogurt.JPG",
                List.of("chili flakes", "sriracha", "Pickled onions","Τραγανό κρεμμύδι"),
                6.90,
                "Food"
        ));

        dishes.put("Crispy Chicken & Egg Fried Rice", new Dish(
                "Crispy Chicken & Egg Fried Rice",
                "Μπουτάκια κοτόπουλου με γιαούρτι & μπαχαρικά.",
                "image/Crispy_Chicken_and_Egg_Fried_Rice.JPG",
                List.of("πίτα", "Πιπέρι καγιέν", "κόκκινο λάχανο τουρσί"),
                12.70,
                "Food"
        ));

        dishes.put("Garlic Butter Fries with Parmesan", new Dish(
                "Garlic Butter Fries with Parmesan",
                "Τηγανητές πατάτες με βούτυρο σκόρδου, παρμεζάνα & φρέσκο μαϊντανό.",
                "image/Crispy_Garlic_Butter_Fries_with_Parmesan_and_Fresh_Parsley.JPG",
                List.of("Τριμμένο καρότο", "Chili flakes", "sriracha","Crispy shallots","Σάλτσα Caesar"),
                4.50,
                "Food"
        ));

        dishes.put("Fresh Avocado Apple & Quinoa Salad", new Dish(
                "Fresh Avocado Apple & Quinoa Salad",
                "Σαλάτα με αβοκάντο, μήλο, κινόα & σάλτσα λεμονιού με μέλι.",
                "image/Fresh_Avocado_Apple_and_Quinoa_Salad.JPG",
                List.of("Τυρί φέτα", "Καρύδια", "τορτίγιες","Σπόροι chia","Κρουτόν"),
                6.80,
                "Food"
        ));

        dishes.put("Spicy Avocado Nachos", new Dish(
                "Spicy Avocado Nachos",
                "Πένες καρμπονάρα με αυγά, κρέμα, παρμεζάνα & τραγανό μπέικον.",
                "image/Spicy_Avocado_Nachos.JPG",
                List.of("Crispy bacon", "Σπόροι ροδιού", "crispy shallots","τυρί φέτα"),
                7.30,
                "Food"
        ));

        dishes.put("Mediterranean Mezze Platter", new Dish(
                "Mediterranean Mezze Platter",
                "Μεσογειακοί μεζέδες με πιτάκια, ελιές, ντοματίνια, ταχίνι & χούμους καρότου.",
                "image/Mediterranean_Mezze_Platter.JPG",
                List.of("Κροκέτες", "Αχλάδι", "Σπόροι ροδιού"),
                8.50,
                "Food"
        ));

        dishes.put("Korean Fried Cauliflower with Edamame", new Dish(
                "Korean Fried Cauliflower with Edamame",
                "Κορεάτικο τηγανητό κουνουπίδι με edamame & γλυκόξινη σάλτσα.",
                "image/Korean_Fried_Cauliflower_with_Edamame.JPG",
                List.of("sriracha", "Τυρί φέτα", "Πιπέρι καγιέν","Μέλι","Λάδι σόγιας"),
                6.40,
                "Food"
        ));

        dishes.put("Buffalo Chicken Fries with Coleslaw", new Dish(
                "Buffalo Chicken Fries with Coleslaw",
                "Crinkle πατάτες με σως Buffalo, κοτόπουλο, κολοσλάου & πίκλες ανίθου.",
                "image/Buffalo_Chicken_Fries_with_Coleslaw_and_Dill_Pickles.JPG",
                List.of("Crispy shallots", "τυρί cheddar", "Πιπέρι καγιέν"),
                10.50,
                "Food"
        ));

        dishes.put("Spicy Chorizo Bites", new Dish(
                "Spicy Chorizo Bites",
                "Κεφτεδάκια chorizo με πιπεριές, πουρέ & πικάντικη tomatillo sauce.",
                "image/Spicy_Chorizo_Bites.JPG",
                List.of("τηγανητά αυγά", "τυρί φέτα", "Ρόδι","Φρέσκο lime"),
                9,
                "Food"
        ));

        dishes.put("Steak & Potato Alfredo Pizza", new Dish(
                "Steak & Potato Alfredo Pizza",
                "Πίτσα με σάλτσα αλφρέντο, μοσχαρίσιο φιλέτο, πατάτες, τρία τυριά & ρόκα.",
                "image/Steak_and_Potato_Alfredo_Pizza.JPG",
                List.of("Φρέσκες φέτες ντομάτας", "Καραμελωμένα κρεμμύδια", "Αχλάδι","τυρί παρμεζάνα"),
                4.80,
                "Food"
        ));

        dishes.put("Twisted Reuben Fries", new Dish(
                "Twisted Reuben Fries",
                "Reuben με corned beef, καραμελωμένα κρεμμύδια, σως ελβετικού τυριού, πατάτες & thousand island.",
                "image/Twiste_Reuben_Fries.JPG",
                List.of("Πιπέρι καπνιστό", "Μαρούλι", "Αβοκάντο","τυρί παρμεζάνα"),
                6.50,
                "Food"
        ));

        dishes.put("Authentic Greek Skordalia", new Dish(
                "Authentic Greek Skordalia",
                "Σκορδαλιά με πουρέ πατάτας, ξηρούς καρπούς, σκόρδο & ελαιόλαδο.",
                "image/Authentic_Greek_Skordalia.JPG",
                Collections.emptyList(),
                3,
                "Food"
        ));

        dishes.put("Flavorful Veggie Tacos", new Dish(
                "Flavorful Veggie Tacos",
                "Τορτίγιες με γλυκοπατάτες, καλαμπόκι, κινόα, queso fresco & γουακαμόλε.",
                "image/Flavorful_Veggie_Tacos.JPG",
                List.of("Σπόρια ροδιού", "Ψητό καλαμπόκι", "τυρί φέτα"),
                11.20,
                "Food"
        ));

        dishes.put("Banh Mi Fries", new Dish(
                "Banh Mi Fries",
                "Lattice fries με κοτόπουλο gochujang & τουρσί λαχανικών.",
                "image/Banh_Mi_Fries.JPG",
                List.of("sriracha", "φιστίκια", "Καραμελωμένα κρεμμύδια"),
                4.20,
                "Food"
        ));

        // Listener για ενημέρωση πλήθους καλαθιού όταν αλλάζει η λίστα
        CartManager.getCartItems().addListener((javafx.collections.ListChangeListener<CartItem>) change -> {
            updateCartItemCountLabel();
        });
        // Αρχική ενημέρωση πλήθους καλαθιού
        updateCartItemCountLabel();


        setupDishImageMap();
        loadDishData();
        // Ενημέρωση ορατότητας δημοφιλών πιάτων
        updateTopDishesVisibility();
    }

    // Ενημερώνει την ετικέτα που δείχνει πόσα προϊόντα υπάρχουν στο καλάθι
    private void updateCartItemCountLabel() {
        if (cartItemCountLabel != null) {
            cartItemCountLabel.setText(String.valueOf(CartManager.getCartItemCount()));
            animateCartItemCountLabel();
        }
    }

    // Κάνει ένα μικρό animation στην ετικέτα του καλαθιού
    private void animateCartItemCountLabel() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), cartItemCountLabel);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.3);
        st.setToY(1.3);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }

    // Μέθοδοι για το click σε κάθε πιάτο. Εμφανίζουν τις επιλογές του πιάτου.
    @FXML
    private void onCheddarClicked() {
        showDishOptions(dishes.get("Cheddar & Beef Omelette"), dishOptionsPane2, 2);
    }

    @FXML
    private void onChorizoClicked() {
        showDishOptions(dishes.get("Chorizo & Potato Hash"), dishOptionsPane1, 1);
    }
    @FXML
    private void onBananaPancakesClicked() {
        showDishOptions(dishes.get("Protein Banana Pancakes"), dishOptionsPane1, 1);
    }
    @FXML
    private void onChocolatePancakesClicked() {
        showDishOptions(dishes.get("Chocolate Protein Pancakes"), dishOptionsPane2, 2);
    }
    @FXML
    private void onFrenchToastClicked() {
        showDishOptions(dishes.get("Protein French Toast"), dishOptionsPane2, 2);
    }
    @FXML
    private void onBerryParfaitClicked() {
        showDishOptions(dishes.get("Berry Protein Parfait"), dishOptionsPane2, 2);
    }
    @FXML
    private void onAvocadoEggToastClicked() {
        showDishOptions(dishes.get("Avocado & Egg Toast"), dishOptionsPane1, 1);
    }
    @FXML
    private void onGreekYogurtPancakesClicked() {
        showDishOptions(dishes.get("Greek Yogurt Protein Pancakes"), dishOptionsPane1, 1);
    }
    @FXML
    private void onCheeseCakePancakesClicked() {
        showDishOptions(dishes.get("Protein Cheesecake Pancakes"), dishOptionsPane1, 1);
    }
    @FXML
    private void onCrispyWafflesClicked() {
        showDishOptions(dishes.get("Crispy Protein Waffles"), dishOptionsPane2, 2);
    }
    @FXML
    private void onBlueberryMuffinsClicked() {
        showDishOptions(dishes.get("Blueberry Protein Muffins"), dishOptionsPane2, 2);
    }
    @FXML
    private void onRaspberryWhiteChocolateMuffinsClicked() {
        showDishOptions(dishes.get("Raspberry & White Chocolate Muffins"), dishOptionsPane1, 1);
    }
    @FXML
    private void onButterChickenClicked() {
        showDishOptions(dishes.get("Butter Chicken"), dishOptionsPane2, 2);
    }
    @FXML
    private void onCajunChickenPastaClicked() {
        showDishOptions(dishes.get("Cajun Chicken Pasta"), dishOptionsPane2, 2);
    }
    @FXML
    private void onClassicCarbonaraClicked() {
        showDishOptions(dishes.get("Classic Carbonara"), dishOptionsPane2, 2);
    }
    @FXML
    private void onChickenCalzoneClicked() {
        showDishOptions(dishes.get("Chicken Calzone"), dishOptionsPane2, 2);
    }
    @FXML
    private void onCreamyBeefMacAndCheeseClicked() {
        showDishOptions(dishes.get("Creamy Beef Mac & Cheese"), dishOptionsPane2, 2);
    }
    @FXML
    private void onChickenTikkaMasalaWithPilauRiceClicked() {
        showDishOptions(dishes.get("Chicken Tikka Masala with Pilau Rice"), dishOptionsPane2, 2);
    }
    @FXML
    private void onSpicyNandoChickenWithGoldenRiceClicked() {
        showDishOptions(dishes.get("Spicy Nando’s Chicken with Golden Rice"), dishOptionsPane1, 1);
    }
    @FXML
    private void onCrispyOrangeChickenWithBasmatiRiceClicked() {
        showDishOptions(dishes.get("Crispy Orange Chicken with Basmati Rice"), dishOptionsPane1, 1);
    }
    @FXML
    private void onSmashBurgerAndCrinkleFriesClicked() {
        showDishOptions(dishes.get("Smash Burger & Crinkle Fries"), dishOptionsPane1, 1);
    }
    @FXML
    private void onCrispyBangBangChickenClicked() {
        showDishOptions(dishes.get("Crispy Bang Bang Chicken"), dishOptionsPane1, 1);
    }
    @FXML
    private void onTandooriKebabsWithRefreshingMintYogurtClicked() {
        showDishOptions(dishes.get("Tandoori Kebabs with Refreshing Mint Yogurt"), dishOptionsPane1, 1);
    }
    @FXML
    private void onCrispyChickenAndEggFriedRiceClicked() {
        showDishOptions(dishes.get("Crispy Chicken & Egg Fried Rice"), dishOptionsPane1, 1);
    }
    @FXML
    private void onGarlicButterFriesWithParmesanClicked() {
        showDishOptions(dishes.get("Garlic Butter Fries with Parmesan"), dishOptionsPane2, 2);
    }
    @FXML
    private void onFreshAvocadoAppleQuinoaSaladClicked() {
        showDishOptions(dishes.get("Fresh Avocado Apple & Quinoa Salad"), dishOptionsPane2, 2);
    }
    @FXML
    private void onSpicyAvocadoNachosClicked() {
        showDishOptions(dishes.get("Spicy Avocado Nachos"), dishOptionsPane2, 2);
    }
    @FXML
    private void onMediterraneanMezzePlatterClicked() {
        showDishOptions(dishes.get("Mediterranean Mezze Platter"), dishOptionsPane2, 2);
    }
    @FXML
    private void onKoreanFriedCauliflowerWithEdamameClicked() {
        showDishOptions(dishes.get("Korean Fried Cauliflower with Edamame"), dishOptionsPane2, 2);
    }
    @FXML
    private void onBuffaloChickenFriesWithColeslawClicked() {
        showDishOptions(dishes.get("Buffalo Chicken Fries with Coleslaw"), dishOptionsPane2, 2);
    }
    @FXML
    private void onSpicyChorizoBitesClicked() {
        showDishOptions(dishes.get("Spicy Chorizo Bites"), dishOptionsPane1, 1);
    }
    @FXML
    private void onSteakAndPotatoAlfredoPizzaClicked() {
        showDishOptions(dishes.get("Steak & Potato Alfredo Pizza"), dishOptionsPane1, 1);
    }
    @FXML
    private void onTwistedReubenFriesClicked() {
        showDishOptions(dishes.get("Twisted Reuben Fries"), dishOptionsPane1, 1);
    }
    @FXML
    private void onAuthenticGreekSkordaliaClicked() {
        showDishOptions(dishes.get("Authentic Greek Skordalia"), dishOptionsPane1, 1);
    }
    @FXML
    private void onFlavorfullVeggieTacosClicked() {
        showDishOptions(dishes.get("Flavorful Veggie Tacos"), dishOptionsPane1, 1);
    }
    @FXML
    private void onBanhMiFriesClicked() {
        showDishOptions(dishes.get("Banh Mi Fries"), dishOptionsPane1, 1);
    }

    private Dish currentlyShownDish1 = null;
    private Dish currentlyShownDish2 = null;

    // Στυλ για τα κουμπιά αύξησης/μείωσης ποσότητας
    private void styleQuantityButton(Button button) {
        button.setStyle("-fx-background-color: #e0e0e0; -fx-font-weight: bold; -fx-background-radius: 6;");
        button.setPrefWidth(30);
    }


    // Προβάλει τις επιλογές για ένα πιάτο
    private void showDishOptions(Dish dish, VBox targetPane, int paneNumber) {
        // Αν το ίδιο πιάτο είναι ήδη εμφανές, το κλείνει με fade out
        Dish currentlyShownDish = (paneNumber == 1) ? currentlyShownDish1 : currentlyShownDish2;
        if (currentlyShownDish != null && currentlyShownDish.equals(dish)) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), targetPane);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                targetPane.setVisible(false);
                targetPane.getChildren().clear();
                if (paneNumber == 1) currentlyShownDish1 = null;
                else currentlyShownDish2 = null;
            });
            fadeOut.play();
            return;
        }

        // Ορίζει ποιο πιάτο προβάλλεται
        if (paneNumber == 1) currentlyShownDish1 = dish;
        else currentlyShownDish2 = dish;

        targetPane.setVisible(true);
        targetPane.setOpacity(1.0);
        targetPane.getChildren().clear();

        // Animation εμφάνισης panel
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), targetPane);
        scaleIn.setFromX(0.9);
        scaleIn.setFromY(0.9);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        scaleIn.play();

        VBox wrapper = new VBox(12);
        wrapper.setPrefWidth(418);
        wrapper.setPrefHeight(714);
        wrapper.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 20; -fx-padding: 20; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 4);");
        wrapper.setAlignment(Pos.TOP_CENTER);

        // Εμφάνιση ονόματος πιάτου
        Label nameLabel = new Label(dish.getName());
        nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Εικόνα πιάτου
        ImageView imageView;
        URL imageUrl = getClass().getResource(dish.getImagePath());
        imageView = (imageUrl != null)
                ? new ImageView(new Image(imageUrl.toExternalForm()))
                : new ImageView();
        imageView.setFitWidth(340);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-background-radius: 12; -fx-border-radius: 12;");

        // Περιγραφή πιάτου
        Label descLabel = new Label(dish.getDescription());
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(360);
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");

        // Τίτλος για extra υλικά
        Label extraLabel = new Label("Extra Υλικά (+0.70€ έκαστο)");
        extraLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        // Λίστα από checkbox για extra υλικά
        VBox ingredientsBox = new VBox(5);
        ingredientsBox.setMaxWidth(360);
        List<CheckBox> ingredientCheckBoxes = new ArrayList<>();
        for (String ingredient : dish.getExtraIngredients()) {
            CheckBox cb = new CheckBox(ingredient);
            cb.setStyle("-fx-font-size: 13px;");
            cb.setCursor(Cursor.HAND);
            ingredientCheckBoxes.add(cb);
            ingredientsBox.getChildren().add(cb);
        }

        // Πεδίο για ειδικές οδηγίες
        Label instructionsLabel = new Label("Ειδικές Οδηγίες");
        instructionsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField instructionsField = new TextField();
        instructionsField.setPromptText("Π.χ. Χωρίς αλάτι");
        instructionsField.setPrefWidth(360);
        instructionsField.setStyle("-fx-background-radius: 8; -fx-padding: 6;");

        // Επιλογή ποσότητας
        Label quantityLabel = new Label("Ποσότητα");
        quantityLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField quantityField = new TextField("1");
        quantityField.setPrefWidth(40);
        quantityField.setAlignment(Pos.CENTER);
        quantityField.setStyle("-fx-background-radius: 8;");

        Button decrease = new Button("-");
        Button increase = new Button("+");
        styleQuantityButton(decrease);
        styleQuantityButton(increase);

        decrease.setCursor(Cursor.HAND);
        increase.setCursor(Cursor.HAND);

        HBox quantityBox = new HBox(10, decrease, quantityField, increase);
        quantityBox.setAlignment(Pos.CENTER);

        // Ετικέτα τιμής
        Label priceLabel = new Label();
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");


        // Συνάρτηση για υπολογισμό και εμφάνιση τιμής
        Runnable updatePrice = () -> {
            int quantity = Integer.parseInt(quantityField.getText());
            long selectedExtras = ingredientCheckBoxes.stream().filter(CheckBox::isSelected).count();
            double totalPrice = (dish.getPrice() + selectedExtras * 0.70) * quantity;
            priceLabel.setText(String.format("Τιμή: %.2f€", totalPrice));
        };

        // Λειτουργίες κουμπιών + -
        decrease.setOnAction(e -> {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity > 1) {
                quantity--;
                quantityField.setText(String.valueOf(quantity));
                updatePrice.run();
            }
        });

        increase.setOnAction(e -> {
            int quantity = Integer.parseInt(quantityField.getText());
            quantity++;
            quantityField.setText(String.valueOf(quantity));
            updatePrice.run();
        });

        // Όταν αλλάζει το check σε extra υλικό → ανανεώνει τιμή
        ingredientCheckBoxes.forEach(cb -> cb.setOnAction(e -> updatePrice.run()));

        // Έλεγχος ώστε να δέχεται μόνο αριθμούς στο quantity
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*") || newVal.isEmpty()) {
                quantityField.setText("1");
            }
            updatePrice.run();
        });

        updatePrice.run();

        // Κουμπί προσθήκης στο καλάθι
        Button addButton = new Button("Προσθήκη στο Καλάθι");
        addButton.setPrefWidth(360);
        addButton.setStyle("-fx-background-color: #d2ab66; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 10; -fx-padding: 10 0; -fx-font-size: 15px;");
        addButton.setCursor(Cursor.HAND);

        addButton.setOnAction(e -> {
            String dishName = dish.getName();
            int quantity = Integer.parseInt(quantityField.getText());
            String description = instructionsField.getText();

            String selectedExtras = ingredientCheckBoxes.stream()
                    .filter(CheckBox::isSelected)
                    .map(CheckBox::getText)
                    .collect(Collectors.joining(", "));

            long extrasCount = ingredientCheckBoxes.stream().filter(CheckBox::isSelected).count();
            double unitPrice = dish.getPrice() + extrasCount * 0.70;

            String category = dish.getCategory();

            CartItem cartItem = new CartItem(dishName, quantity, description, unitPrice, selectedExtras, category);

            CartManager.addItemToCart(cartItem);

            // Κλείσιμο panel μετά την προσθήκη
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), targetPane);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event2 -> {
                targetPane.setVisible(false);
                targetPane.getChildren().clear();
                if (paneNumber == 1) currentlyShownDish1 = null;
                else currentlyShownDish2 = null;
            });
            fadeOut.play();
        });

        // Προσθήκη όλων των components στο wrapper
        wrapper.getChildren().addAll(
                nameLabel,
                imageView,
                descLabel,
                extraLabel,
                ingredientsBox,
                instructionsLabel,
                instructionsField,
                quantityLabel,
                quantityBox,
                priceLabel,
                addButton
        );

        // Προσθήκη του wrapper στο panel
        targetPane.getChildren().add(wrapper);
    }

    // Χάρτης για να θυμόμαστε ποιο comment box (ποτά) είναι ανοιχτό σε κάθε Pane
    private Map<Pane, String> currentCommentBoxMap = new HashMap<>();

    private void showDishCommentBox(String dishName, double pricePerUnit, Pane targetPane, String category) {
        // Αν έχει ήδη ανοιχτό comment box για το ίδιο πιάτο, το κλείνει
        if (dishName.equals(currentCommentBoxMap.get(targetPane))) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), targetPane);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                targetPane.setVisible(false);
                targetPane.getChildren().clear();
                currentCommentBoxMap.remove(targetPane);
            });
            fadeOut.play();
            return;
        }

        // Ανοίγει καινούριο comment box για το συγκεκριμένο πιάτο
        currentCommentBoxMap.put(targetPane, dishName);
        targetPane.setVisible(true);
        targetPane.toFront();
        targetPane.setOpacity(1.0);
        targetPane.getChildren().clear();

        // Εφέ εμφάνισης
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), targetPane);
        scaleIn.setFromX(0.9);
        scaleIn.setFromY(0.9);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        scaleIn.play();

        // Το wrapper VBox που κρατάει όλα τα components του comment box
        VBox wrapper = new VBox(12);
        wrapper.setPrefWidth(418);
        wrapper.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 20; -fx-padding: 20; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");
        wrapper.setAlignment(Pos.TOP_CENTER);

        // Τίτλος πιάτου
        Label nameLabel = new Label(dishName);
        nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Label και TextField για σχόλια
        Label instructionsLabel = new Label("Σχόλια / Οδηγίες");
        instructionsLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField instructionsField = new TextField();
        instructionsField.setPromptText("Π.χ. Καλά ψημένο");
        instructionsField.setPrefWidth(360);
        instructionsField.setStyle("-fx-background-radius: 8; -fx-padding: 6;");

        // Label και πεδίο για ποσότητα
        Label quantityLabel = new Label("Ποσότητα");
        quantityLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField quantityField = new TextField("1");
        quantityField.setPrefWidth(40);
        quantityField.setAlignment(Pos.CENTER);
        quantityField.setStyle("-fx-background-radius: 8;");

        // Κουμπιά αύξησης και μείωσης ποσότητας
        Button decrease = new Button("-");
        Button increase = new Button("+");
        styleQuantityButton(decrease);
        styleQuantityButton(increase);

        decrease.setCursor(Cursor.HAND);
        increase.setCursor(Cursor.HAND);

        HBox quantityBox = new HBox(10, decrease, quantityField, increase);
        quantityBox.setAlignment(Pos.CENTER);

        // Label για τιμή
        Label priceLabel = new Label();
        priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Runnable που υπολογίζει και ενημερώνει την τιμή βάση ποσότητας
        Runnable updatePrice = () -> {
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException e) {
                quantity = 1;
                quantityField.setText("1");
            }
            double totalPrice = pricePerUnit * quantity;
            priceLabel.setText(String.format("Τιμή: %.2f€", totalPrice));
        };

        decrease.setOnAction(e -> {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity > 1) {
                quantity--;
                quantityField.setText(String.valueOf(quantity));
                updatePrice.run();
            }
        });

        increase.setOnAction(e -> {
            int quantity = Integer.parseInt(quantityField.getText());
            quantity++;
            quantityField.setText(String.valueOf(quantity));
            updatePrice.run();
        });

        // Listener για να μη βάζει άκυρες τιμές στο quantity field
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*") || newVal.isEmpty()) {
                quantityField.setText("1");
            }
            updatePrice.run();
        });

        updatePrice.run();

        // Κουμπί προσθήκης στο καλάθι
        Button addButton = new Button("Προσθήκη στο Καλάθι");
        addButton.setPrefWidth(360);
        addButton.setStyle("-fx-background-color: #d2ab66; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 10; -fx-padding: 10 0; -fx-font-size: 15px;");
        addButton.setCursor(Cursor.HAND);

        // Action όταν πατηθεί το addButton
        addButton.setOnAction(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                String description = instructionsField.getText();

                Dish dish = dishes.get(dishName);

                CartItem cartItem = new CartItem(
                        dishName,
                        quantity,
                        description,
                        pricePerUnit,
                        "",
                        category
                );

                CartManager.addItemToCart(cartItem);

                // Κλείνει το comment box με fade out
                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), targetPane);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(event2 -> {
                    targetPane.setVisible(false);
                    targetPane.getChildren().clear();
                    currentCommentBoxMap.remove(targetPane);
                });
                fadeOut.play();

            } catch (NumberFormatException ex) {
                System.out.println("Invalid quantity input.");
            }
        });

        wrapper.getChildren().addAll(
                nameLabel,
                instructionsLabel,
                instructionsField,
                quantityLabel,
                quantityBox,
                priceLabel,
                addButton
        );

        targetPane.getChildren().add(wrapper);
    }

    // Οι @FXML μέθοδοι για τα κλικ events των προϊόντων για τα ποτά.
    @FXML private void onVegaSiciliaClicked() { showDishCommentBox("Vega Sicilia", 8.50, dishCommentPane1, "Beverages"); }
    @FXML private void onTiggnanelloClicked() {
        showDishCommentBox("Tignanello", 90, dishCommentPane1, "Beverages");
    }
    @FXML private void onCatenaZapataClicked() { showDishCommentBox("Catena Zapata", 95, dishCommentPane1, "Beverages"); }
    @FXML private void onKathrynHallClicked() { showDishCommentBox("Kathryn Hall", 100, dishCommentPane1, "Beverages");}
    @FXML private void onPinotNoirClicked() {
        showDishCommentBox("Pinot Noir", 105, dishCommentPane1, "Beverages");
    }
    @FXML private void onZinfandelClicked() {
        showDishCommentBox("ZINFANDEL", 160, dishCommentPane1, "Beverages");
    }

    @FXML private void onSauvignonBlancClicked() { showDishCommentBox("Sauvignon Blanc", 80, dishCommentPane2, "Beverages"); }
    @FXML private void onCapensisChardonnayClicked() { showDishCommentBox("Capensis Chardonnay", 60, dishCommentPane2, "Beverages"); }
    @FXML private void onOrnellaiaBiancoClicked() { showDishCommentBox("Ornellaia Bianco", 80, dishCommentPane2, "Beverages"); }
    @FXML private void onAmeixbarVulcaoClicked() { showDishCommentBox("Ameixbar Vulcao", 90, dishCommentPane2, "Beverages"); }
    @FXML private void onPinotGrigioClicked() { showDishCommentBox("Pinot Grigio", 110, dishCommentPane2, "Beverages"); }
    @FXML private void onRieslingClicked() {
        showDishCommentBox("Riesling", 135, dishCommentPane2, "Beverages");
    }

    @FXML private void onLeFragheRodonClicked() { showDishCommentBox("Le Fraghe Rodon", 200, dishCommentPane3, "Beverages"); }
    @FXML private void onGabrielskloofClicked() { showDishCommentBox("Gabrielskloof", 95, dishCommentPane3, "Beverages"); }
    @FXML private void onBargemoneClicked() {
        showDishCommentBox("Bargemone", 108, dishCommentPane3, "Beverages");
    }
    @FXML private void onChateauDeChaintresClicked() { showDishCommentBox("Château De Chaintres", 140, dishCommentPane3, "Beverages"); }
    @FXML private void onGrolleauClicked() {
        showDishCommentBox("Grolleau", 58, dishCommentPane3, "Beverages");
    }
    @FXML private void onJoshCellarsRoseClicked() { showDishCommentBox("Josh Cellars Rose", 95, dishCommentPane3, "Beverages"); }

    @FXML private void onCocaColaClicked() {
        showDishCommentBox("Coca Cola", 2.50, dishCommentPane4, "Beverages");
    }
    @FXML private void onCocaColaZerofClicked() { showDishCommentBox("Coca Cola Zero", 2.50, dishCommentPane4, "Beverages"); }
    @FXML private void onPepsiClicked() {
        showDishCommentBox("Pepsi", 2.50, dishCommentPane4, "Beverages");
    }
    @FXML private void onPepsiZeroClicked() {
        showDishCommentBox("Pepsi Zero", 2.50, dishCommentPane4, "Beverages");
    }
    @FXML private void onFantaOrangeClicked() { showDishCommentBox("Fanta Orange", 2.70, dishCommentPane4, "Beverages"); }
    @FXML private void onFantaExoticClicked() { showDishCommentBox("Fanta Exotic", 2.70, dishCommentPane4, "Beverages"); }
    @FXML private void onFantaStrawberryKiwiClicked() { showDishCommentBox("Fanta Strawberry Kiwi", 3, dishCommentPane4, "Beverages"); }
    @FXML private void onSpriteClicked() {
        showDishCommentBox("Sprite", 2.70, dishCommentPane5, "Beverages");
    }
    @FXML private void onSchweppesPinkClicked() { showDishCommentBox("Schweppes Pink", 3.50, dishCommentPane5, "Beverages"); }
    @FXML private void onSchweppesLeomnadeClicked() { showDishCommentBox("Schweppes Leomnade", 3.50, dishCommentPane5, "Beverages"); }
    @FXML private void onSchweppesSodaClicked() { showDishCommentBox("Schweppes Soda", 3.50, dishCommentPane5, "Beverages"); }
    @FXML private void onLiptonIceTeaLemonClicked() { showDishCommentBox("Lipton Ice Tea Lemon", 2.90, dishCommentPane5, "Beverages"); }
    @FXML private void onLiptonIceTeaGreenClicked() { showDishCommentBox("Lipton Ice Tea Green", 2.90, dishCommentPane5, "Beverages"); }
    @FXML private void onLiptonIceTeaPeachClicked() { showDishCommentBox("Lipton Ice Tea Peach", 2.90, dishCommentPane5, "Beverages"); }
    @FXML private void onLiptonIceTeaPeachZeroClicked() {showDishCommentBox("Lipton Ice Tea Peach Zero", 2.90, dishCommentPane5, "Beverages"); }
    @FXML private void onSanPellegrinoClicked() { showDishCommentBox("San Pellegrino", 1.50, dishCommentPane5, "Beverages"); }
    @FXML private void onWaterClicked() {
        showDishCommentBox("Water", 1, dishCommentPane5, "Beverages");
    }

    @FXML private void onBecksClicked() {
        showDishCommentBox("Becks", 4, dishCommentPane6, "Beverages");
    }
    @FXML private void onAlfaClicked() {
        showDishCommentBox("Alfa", 4.20, dishCommentPane6, "Beverages");
    }
    @FXML private void onBrahmaClicked() {
        showDishCommentBox("Brahma", 4.60, dishCommentPane6, "Beverages");
    }
    @FXML private void onHarbinClicked() {
        showDishCommentBox("Harbin", 6.50, dishCommentPane6, "Beverages");
    }
    @FXML private void onHeinekenClicked() {
        showDishCommentBox("Heineken", 3.8, dishCommentPane6, "Beverages");
    }
    @FXML private void onCoronaClicked() {
        showDishCommentBox("Corona", 7, dishCommentPane6, "Beverages");
    }
}