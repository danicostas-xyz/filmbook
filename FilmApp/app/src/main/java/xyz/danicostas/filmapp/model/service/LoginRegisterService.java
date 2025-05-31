package xyz.danicostas.filmapp.model.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.model.persistence.DaoUser;
import xyz.danicostas.filmapp.view.activity.ApplicationActivity;
import xyz.danicostas.filmapp.view.activity.LoginActivity;
import xyz.danicostas.filmapp.view.loader.CustomAlertDialog;
import xyz.danicostas.filmapp.viewmodel.FilmListsViewModel;

public class LoginRegisterService {
    private FirebaseAuth mAuth;
    private DaoUser daoUser;
    private UserService userService;
    private static LoginRegisterService instance;

    private LoginRegisterService() {
        mAuth = FirebaseAuth.getInstance();
        daoUser = DaoUser.getInstance();
        userService = UserService.getInstance();
    }

    public static LoginRegisterService getInstance() {
        return instance == null ? instance = new LoginRegisterService() : instance;
    }

    public void validateLogin(Context context) {
        if (mAuth.getCurrentUser() != null) {
            userService.getUserData(mAuth.getCurrentUser().getUid());
            Intent intent = new Intent(context, ApplicationActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * Authenticates a user using Firebase Authentication and initializes the user session.
     *
     * If authentication is successful:
     *    - Retrieves the authenticated user’s information.
     *    - Initializes the user session by collecting relevant data.
     *    - Redirects the user to the main Application Activity.
     *
     * If authentication fails, an error message is displayed.
     *
     * @param context  the Activity context from where the method is called, used for navigation.
     * @param email    the email address of the user attempting to sign in.
     * @param password the password of the user attempting to sign in.
     */
    public void login(Context context, String email, String password) {

        CustomAlertDialog progressDialog = new CustomAlertDialog(context, context.getString(R.string.logging));
        progressDialog.show();

        if (mAuth != null) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        userService.getUserData(mAuth.getCurrentUser().getUid());
                        Intent intent = new Intent(context, ApplicationActivity.class);
                        progressDialog.dismiss(1000L);
                        context.startActivity(intent);

                    } else {
                        String errorMessage = task.getException() != null
                                ? task.getException().getMessage()
                                : "Invalid Credentials";
                        Log.e("GestorUser", "Login failed: " + errorMessage);
                        // Toast.makeText(context, context.getString(R.string.loginKO) + " " + errorMessage, Toast.LENGTH_SHORT).show();
                        progressDialog.setText("Email y/o contraseña incorrectos \uD83D\uDE14");
                        // TODO
                        // Aquí hay que hacer también un progressDialog.dismiss(), pero lo dejo así,
                        // para poder enseñar el loader de forma indefinida cuando el inicio de
                        // sesión es KO.
                    }
                });

        } else {
            Log.e("GestorUser", "FirebaseAuth is not initialized.");
            progressDialog.dismiss(1000L);
        }

    }

    /**
     * Logs out the current user from Firebase Authentication and clears the user session.
     *
     * If Firebase Authentication (`mAuth`) is not initialized, an error is logged.
     *
     * @param context the Activity context from which the method is called, used for navigation.
     */
    public void logout(Context context) {
        if (mAuth != null) {
            mAuth.signOut();
            Log.d("LOGOUT", "CERRANDO SESIÓN");
            UserSession.getInstance().clearUserData();
            FilmListsViewModel.notifyUserLoggedOut();
            context.startActivity(new Intent(context, LoginActivity.class));
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        } else {
            Log.e("GestorUser", "FirebaseAuth is not initialized.");
        }
    }

    /**
     * Registers a new user using Firebase Authentication and saves the user's data in Firestore.
     * This method first creates a user with the provided email and password. If the authentication
     * is successful, it initializes the user's session and stores additional user information in Firestore.
     * Additionally, it also checks if the email is already in the Database.
     *
     * @param context  the Activity context from where the method is called.
     * @param email    the email address used for authentication.
     * @param username the username to be associated with the user.
     * @param password the password used for authentication.
     */
    public void register(Context context, String email, String username, String password) {
        if (mAuth != null) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(authTask -> {
                    if (authTask.isSuccessful()) {

                        if (mAuth.getCurrentUser() != null) {
                            UserSession.getInstance().setUser("Name", mAuth.getCurrentUser().getUid(),email);
                            UserSession.getInstance().setUsername(username);

                            User user = new User();
                            List<FilmList> filmLists = mockListOfLists();
                            List<Review> listOfReviews = mockListOfReviews();
                            user.setId(UserSession.getInstance().getUserId());
                            user.setListasDeListas(filmLists);
                            user.setUsername(UserSession.getInstance().getUsernameLiveData().getValue());
                            user.setListOfReviews(listOfReviews);
                            user.setEmail(UserSession.getInstance().getEmail());
                            user.setName(UserSession.getInstance().getName());
                            user.setListaAmigos(new ArrayList<User>());

                            daoUser.createUser(user.getId(), user, task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, context.getString(R.string.registerOK), Toast.LENGTH_SHORT).show();
                                    if (context instanceof Activity) {
                                        ((Activity) context).finish();
                                    }
                                } else {
                                    Exception exception = task.getException();
                                    String errorMessage = exception != null ? exception.getMessage() : "Unknown error";
                                    Toast.makeText(context, context.getString(R.string.registerKO) + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        String errorMessage = authTask.getException() != null ? authTask.getException().getMessage() : "Unknown error";
                        if (errorMessage.contains("The email address is already in use")) {
                            Toast.makeText(context, context.getString(R.string.mailAlreadyInUse), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.authKO) + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    private List<Review> mockListOfReviews() {
        Review review1 = new Review(
                1,
                "user123",
                "Lynch en su máxima expresión",
                "Una pesadilla surrealista llena de simbolismo y atmósfera inquietante. Una obra maestra del cine experimental.",
                "/mxveW3mGVc0DzLdOmtkZsgd7c3B.jpg",
                985,
                "Eraserhead",
                new Date(),
                9
        );

        Review review2 = new Review(
                2,
                "user456",
                "Una obra maestra del cine neo-noir",
                "Un retrato psicológico brutal de la alienación y la locura en una ciudad decadente. De Niro está impresionante.",
                "/ekstpH614fwDX8DUln1a2Opz0N8.jpg",
                103,
                "Taxi Driver",
                new Date(),
                10
        );

        Review review3 = new Review(
                3,
                "user789",
                "El espíritu de la infancia en su máxima expresión",
                "Una historia encantadora, llena de magia y calidez. Perfecta para todas las edades.",
                "/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg",
                8392,
                "Mi vecino Totoro",
                new Date(),
                8
        );

        return List.of(review1, review2, review3);

    }

    @NonNull
    private List<FilmList> mockListOfLists() {

        // Favoritas
        Film film1 = new Film();
        film1.setId(985);
        film1.setOverview("A gothic tale of obsession between a haunted young woman and the terrifying vampire infatuated with her, causing untold horror in its wake.");
        film1.setOriginalTitle("Eraserhead");
        film1.setPosterPath("https://image.tmdb.org/t/p/w500/mxveW3mGVc0DzLdOmtkZsgd7c3B.jpg");
        film1.setReleaseDate("2024-12-25");
        film1.setTitle("Nosferatu");
        film1.setVoteAverage(6.7);
        film1.setVoteCount(2002);

        Film film2 = new Film();
        film2.setId(8392);
        film2.setOverview("A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.");
        film2.setOriginalTitle("Totoro");
        film2.setPosterPath("https://image.tmdb.org/t/p/w500/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg");
        film2.setReleaseDate("2009-12-18");
        film2.setTitle("Avatar");
        film2.setVoteAverage(7.8);
        film2.setVoteCount(25000);

        Film film3 = new Film();
        film3.setId(103);
        film3.setOverview("An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.");
        film3.setOriginalTitle("Taxi Driver");
        film3.setPosterPath("https://image.tmdb.org/t/p/w500/ekstpH614fwDX8DUln1a2Opz0N8.jpg");
        film3.setReleaseDate("1999-10-15");
        film3.setTitle("Fight Club");
        film3.setVoteAverage(8.8);
        film3.setVoteCount(24000);

        Film film4 = new Film();
        film4.setId(345);
        film4.setOverview("A twisted odyssey through the wreckage of post-industrial America.");
        film4.setOriginalTitle("Eyes Wide Shut");
        film4.setPosterPath("https://image.tmdb.org/t/p/w500/knEIz1eNGl5MQDbrEAVWA7iRqF9.jpg");
        film4.setReleaseDate("1977-03-19");
        film4.setTitle("Eraserhead");
        film4.setVoteAverage(7.5);
        film4.setVoteCount(1300);

        Film film5 = new Film();
        film5.setId(429200);
        film5.setOverview("A group of high schoolers discover that their dreams are being invaded by a killer.");
        film5.setOriginalTitle("Good Time");
        film5.setPosterPath("https://image.tmdb.org/t/p/w500/yE1c9hj5Hf8a9KplAdRdhADqUro.jpg");
        film5.setReleaseDate("1984-11-09");
        film5.setTitle("A Nightmare on Elm Street");
        film5.setVoteAverage(7.3);
        film5.setVoteCount(5000);

        Film film6 = new Film();
        film6.setId(793);
        film6.setOverview("A thief and a group of strangers try to pull off a heist during a massive hurricane.");
        film6.setOriginalTitle("Blue Velvet");
        film6.setPosterPath("https://image.tmdb.org/t/p/w500/7hlgzJXLgyECS1mk3LSN3E72l5N.jpg");
        film6.setReleaseDate("2018-03-09");
        film6.setTitle("The Hurricane Heist");
        film6.setVoteAverage(5.0);
        film6.setVoteCount(500);

        Film film7 = new Film();
        film7.setId(694);
        film7.setOverview("A young woman is forced to team up with a criminal in order to survive a ruthless city.");
        film7.setOriginalTitle("The Shining");
        film7.setPosterPath("https://image.tmdb.org/t/p/w500/mmd1HnuvAzFc4iuVJcnBrhDNEKr.jpg.jpg");
        film7.setReleaseDate("2018-09-07");
        film7.setTitle("Peppermint");
        film7.setVoteAverage(6.5);
        film7.setVoteCount(2000);

        Film film8 = new Film();
        film8.setId(62);
        film8.setOverview("A disillusioned soldier faces difficult choices as a dangerous plot unravels in a futuristic dystopia.");
        film8.setOriginalTitle("2001 A Space Odyssey");
        film8.setPosterPath("https://image.tmdb.org/t/p/w500/w5IDXtifKntw0ajv2co7jFlTQDM.jpg");
        film8.setReleaseDate("2018-03-29");
        film8.setTitle("Ready Player One");
        film8.setVoteAverage(7.5);
        film8.setVoteCount(50000);

        Film film9 = new Film();
        film9.setId(426);
        film9.setOverview("A team of scientists explore an uncharted planet, discovering dangerous wildlife and a race of mysterious humanoid creatures.");
        film9.setOriginalTitle("Vertigo");
        film9.setPosterPath("https://image.tmdb.org/t/p/w500/15uOEfqBNTVtDUT7hGBVCka0rZz.jpg");
        film9.setReleaseDate("2017-05-12");
        film9.setTitle("Alien: Covenant");
        film9.setVoteAverage(6.4);
        film9.setVoteCount(25000);

        // Anime
        Film film10 = new Film();
        film10.setId(118412);
        film10.setOverview("A woman is forced to choose between saving her husband or saving the world.");
        film10.setOriginalTitle("Berserk");
        film10.setPosterPath("https://image.tmdb.org/t/p/w500/gzVQQaDazukAcmiFx6l9WLj1kwo.jpg");
        film10.setReleaseDate("2014-09-19");
        film10.setTitle("The Maze Runner");
        film10.setVoteAverage(6.8);
        film10.setVoteCount(45000);

        Film film11 = new Film();
        film11.setId(129);
        film11.setOverview("In a dystopian future where books are banned, one fireman seeks to preserve knowledge.");
        film11.setOriginalTitle("Spirited Away");
        film11.setPosterPath("https://image.tmdb.org/t/p/w500/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg");
        film11.setReleaseDate("2018-05-19");
        film11.setTitle("Fahrenheit 451");
        film11.setVoteAverage(6.1);
        film11.setVoteCount(5000);

        Film film12 = new Film();
        film12.setId(11621);
        film12.setOverview("A disillusioned writer and his wife live a quiet, isolated life in the countryside, where a series of strange occurrences test their sanity.");
        film12.setOriginalTitle("Porco Rosso");
        film12.setPosterPath("https://image.tmdb.org/t/p/w500/8mIvSvnVBApfORL9N6S38Q7wD6A.jpg");
        film12.setReleaseDate("1980-05-23");
        film12.setTitle("The Shining");
        film12.setVoteAverage(8.4);
        film12.setVoteCount(30000);

        Film film13 = new Film();
        film13.setId(149);
        film13.setOverview("A young woman gets tangled in a web of conspiracy and deadly secrets while investigating the mysterious disappearance of her twin sister.");
        film13.setOriginalTitle("Akira");
        film13.setPosterPath("https://image.tmdb.org/t/p/w500/neZ0ykEsPqxamsX6o5QNUFILQrz.jpg");
        film13.setReleaseDate("2018-04-10");
        film13.setTitle("The Secret");
        film13.setVoteAverage(7.2);
        film13.setVoteCount(4000);

        Film film14 = new Film();
        film14.setId(10494);
        film14.setOverview("A young woman becomes involved with a man whose troubled past brings danger into their lives.");
        film14.setOriginalTitle("Perfect Blue");
        film14.setPosterPath("https://image.tmdb.org/t/p/w500/6WTiOCfDPP8XV4jqfloiVWf7KHq.jpg");
        film14.setReleaseDate("1972-03-24");
        film14.setTitle("The Godfather");
        film14.setVoteAverage(9.2);
        film14.setVoteCount(100000);

        Film film15 = new Film();
        film15.setId(18491);
        film15.setOverview("A group of kids in a small town discover that their neighbor is a vampire.");
        film15.setOriginalTitle("The End of Evangelion");
        film15.setPosterPath("https://image.tmdb.org/t/p/w500/j6G24dqI4WgUtChhWjfnI4lnmiK.jpg");
        film15.setReleaseDate("1987-07-31");
        film15.setTitle("The Lost Boys");
        film15.setVoteAverage(7.3);
        film15.setVoteCount(15000);

        Film film16 = new Film();
        film16.setId(128);
        film16.setOverview("A group of children discover a magical land in a wardrobe and become involved in a battle between good and evil.");
        film16.setOriginalTitle("Mononoke");
        film16.setPosterPath("https://image.tmdb.org/t/p/w500/cMYCDADoLKLbB83g4WnJegaZimC.jpg");
        film16.setReleaseDate("2005-12-09");
        film16.setTitle("The Chronicles of Narnia: The Lion, the Witch and the Wardrobe");
        film16.setVoteAverage(7.1);
        film16.setVoteCount(30000);

        Film film17 = new Film();
        film17.setId(10228);
        film17.setOverview("A secret agent embarks on a mission to stop a dangerous international conspiracy.");
        film17.setOriginalTitle("Pokemon 1");
        film17.setPosterPath("https://image.tmdb.org/t/p/w500/6YPzBcMH0aPNTvdXNCDLY0zdE1g.jpg");
        film17.setReleaseDate("1996-05-22");
        film17.setTitle("Mission: Impossible");
        film17.setVoteAverage(6.9);
        film17.setVoteCount(25000);

        Film film18 = new Film();
        film18.setId(16859);
        film18.setOverview("A group of astronauts gets trapped in space and must fight to survive.");
        film18.setOriginalTitle("Kiki's Delivery Service");
        film18.setPosterPath("https://image.tmdb.org/t/p/w500/Aufa4YdZIv4AXpR9rznwVA5SEfd.jpg");
        film18.setReleaseDate("2013-10-04");
        film18.setTitle("Gravity");
        film18.setVoteAverage(7.7);
        film18.setVoteCount(60000);

        Film film19 = new Film();
        film19.setId(4935);
        film19.setOverview("A warrior fights to save his city from destruction by a deadly force of ancient enemies.");
        film19.setOriginalTitle("Howl's Moving Castle");
        film19.setPosterPath("https://image.tmdb.org/t/p/w500/23hUJh7JdO23SpgUB5oiFDQk2wX.jpg");
        film19.setReleaseDate("2006-03-09");
        film19.setTitle("300");
        film19.setVoteAverage(7.6);
        film19.setVoteCount(100000);

        // Recientes
        Film film20 = new Film();
        film20.setId(1018);
        film20.setOverview("A war between two factions erupts after a devastating attack on a peaceful village.");
        film20.setOriginalTitle("Mulholland Drive");
        film20.setPosterPath("https://image.tmdb.org/t/p/w500/x7A59t6ySylr1L7aubOQEA480vM.jpg");
        film20.setReleaseDate("2005-05-19");
        film20.setTitle("Star Wars: Episode III - Revenge of the Sith");
        film20.setVoteAverage(7.7);
        film20.setVoteCount(250000);

        Film film21 = new Film();
        film21.setId(426063);
        film21.setOverview("A young woman embarks on a mission to retrieve an ancient artifact that has the power to change the world.");
        film21.setOriginalTitle("Nosferatu");
        film21.setPosterPath("https://image.tmdb.org/t/p/w500/5qGIxdEO841C0tdY8vOdLoRVrr0.jpg");
        film21.setReleaseDate("2018-03-16");
        film21.setTitle("Tomb Raider");
        film21.setVoteAverage(6.3);
        film21.setVoteCount(30000);

        Film film22 = new Film();
        film22.setId(1109255);
        film22.setOverview("The story of an unlikely hero who seeks to save humanity from a global extinction.");
        film22.setOriginalTitle("Parthenope");
        film22.setPosterPath("https://image.tmdb.org/t/p/w500/5SqICqUQaYFeZs1UnBZ4AN6DwcP.jpg");
        film22.setReleaseDate("1999-03-31");
        film22.setTitle("The Matrix");
        film22.setVoteAverage(8.7);
        film22.setVoteCount(500000);

        Film film23 = new Film();
        film23.setId(4348);
        film23.setOverview("A newlywed couple’s honeymoon turns into a nightmare as a vengeful ghost pursues them.");
        film23.setOriginalTitle("Pride and Prejudice");
        film23.setPosterPath("https://image.tmdb.org/t/p/w500/sGjIvtVvTlWnia2zfJfHz81pZ9Q.jpg");
        film23.setReleaseDate("2020-10-09");
        film23.setTitle("Blue Velvet");
        film23.setVoteAverage(7.5);
        film23.setVoteCount(15000);

        Film film24 = new Film();
        film24.setId(626332);
        film24.setOverview("An unassuming hotel manager must fight off criminals who try to steal a precious jewel hidden in his hotel.");
        film24.setOriginalTitle("Flamin' Hot");
        film24.setPosterPath("https://image.tmdb.org/t/p/w500/a7KyFMPXj0iY4EoLq1PIGU1WJPw.jpg");
        film24.setReleaseDate("2014-03-28");
        film24.setTitle("The Grand Budapest Hotel");
        film24.setVoteAverage(8.1);
        film24.setVoteCount(40000);

        Film film25 = new Film();
        film25.setId(1359);
        film25.setOverview("A team of adventurers seeks to explore a lost city beneath the desert sands.");
        film25.setOriginalTitle("American Psycho");
        film25.setPosterPath("https://image.tmdb.org/t/p/w500/9uGHEgsiUXjCNq8wdq4r49YL8A1.jpg");
        film25.setReleaseDate("2017-06-09");
        film25.setTitle("The Mummy");
        film25.setVoteAverage(6.1);
        film25.setVoteCount(25000);

        Film film26 = new Film();
        film26.setId(395);
        film26.setOverview("A hero with supernatural abilities teams up with a group of warriors to protect a magical kingdom from evil forces.");
        film26.setOriginalTitle("AVP: Alien vs. Predator");
        film26.setPosterPath("https://image.tmdb.org/t/p/w500/ySWu5bCnnmgV1cVacvFnFIhgOjp.jpg");
        film26.setReleaseDate("2017-10-25");
        film26.setTitle("Thor: Ragnarok");
        film26.setVoteAverage(7.9);
        film26.setVoteCount(400000);

        Film film27 = new Film();
        film27.setId(106);
        film27.setOverview("A young girl embarks on a quest to defeat a terrifying dragon and save her village.");
        film27.setOriginalTitle("Predator");
        film27.setPosterPath("https://image.tmdb.org/t/p/w500/k3mW4qfJo6SKqe6laRyNGnbB9n5.jpg");
        film27.setReleaseDate("2010-03-26");
        film27.setTitle("How to Train Your Dragon");
        film27.setVoteAverage(8.1);
        film27.setVoteCount(150000);

        Film film28 = new Film();
        film28.setId(646385);
        film28.setOverview("A space captain and his crew are sent on a mission to explore new galaxies in search of new life.");
        film28.setOriginalTitle("Scream");
        film28.setPosterPath("https://image.tmdb.org/t/p/w500/1m3W6cpgwuIyjtg5nSnPx7yFkXW.jpg");
        film28.setReleaseDate("2009-05-08");
        film28.setTitle("Star Trek");
        film28.setVoteAverage(7.9);
        film28.setVoteCount(250000);

        Film film29 = new Film();
        film29.setId(9426);
        film29.setOverview("An elite squad of soldiers battles an ancient, mysterious force that threatens the fate of the world.");
        film29.setOriginalTitle("The Fly");
        film29.setPosterPath("https://image.tmdb.org/t/p/w500/8gZWMhJHRvaXdXsNhERtqNHYpH3.jpg");
        film29.setReleaseDate("2010-08-13");
        film29.setTitle("The Expendables");
        film29.setVoteAverage(6.4);
        film29.setVoteCount(35000);

        Film film30 = new Film();
        film30.setId(36647);
        film30.setOverview("A criminal mastermind threatens to bring chaos to Gotham City, and it's up to Batman to stop him.");
        film30.setOriginalTitle("Blade");
        film30.setPosterPath("https://image.tmdb.org/t/p/w500/oWT70TvbsmQaqyphCZpsnQR7R32.jpg");
        film30.setReleaseDate("2008-07-18");
        film30.setTitle("The Dark Knight");
        film30.setVoteAverage(8.8);
        film30.setVoteCount(120000);

        // Pendientes

        Film film31 = new Film();
        film31.setId(205321);
        film31.setOverview("A group of high school students must band together to survive a zombie apocalypse.");
        film31.setOriginalTitle("Sharknado");
        film31.setPosterPath("https://image.tmdb.org/t/p/w500/atEmHkVFTSGRYt2PeCiziQqbZnI.jpg");
        film31.setReleaseDate("1985-02-15");
        film31.setTitle("The Breakfast Club");
        film31.setVoteAverage(7.9);
        film31.setVoteCount(25000);

        Film film32 = new Film();
        film32.setId(348);
        film32.setOverview("An orphaned young woman discovers her true identity while facing a dark, magical curse.");
        film32.setOriginalTitle("Alien");
        film32.setPosterPath("https://image.tmdb.org/t/p/w500/vfrQk5IPloGg1v9Rzbh2Eg3VGyM.jpg");
        film32.setReleaseDate("1987-09-25");
        film32.setTitle("The Princess Bride");
        film32.setVoteAverage(8.1);
        film32.setVoteCount(40000);

        Film film33 = new Film();
        film33.setId(679);
        film33.setOverview("A group of misfit teenagers are forced to save the world from a vengeful alien.");
        film33.setOriginalTitle("Aliens");
        film33.setPosterPath("https://image.tmdb.org/t/p/w500/r1x5JGpyqZU8PYhbs4UcrO1Xb6x.jpg");
        film33.setReleaseDate("2014-07-30");
        film33.setTitle("Guardians of the Galaxy");
        film33.setVoteAverage(7.9);
        film33.setVoteCount(650000);

        Film film34 = new Film();
        film34.setId(8077);
        film34.setOverview("A detective investigates the mysterious death of a beautiful woman who may be the victim of a notorious serial killer.");
        film34.setOriginalTitle("Alien³");
        film34.setPosterPath("https://image.tmdb.org/t/p/w500/xh5wI0UoW7DfS1IyLy3d2CgrCEP.jpg");
        film34.setReleaseDate("1995-09-22");
        film34.setTitle("Se7en");
        film34.setVoteAverage(8.6);
        film34.setVoteCount(600000);

        Film film35 = new Film();
        film35.setId(11351);
        film35.setOverview("A simple man’s life is turned upside down when he gets caught in a high-stakes conspiracy involving political corruption.");
        film35.setOriginalTitle("Jeepers Creepers 2");
        film35.setPosterPath("https://image.tmdb.org/t/p/w500/u2ghDfjcs3y5c8ata3COc4pWiAN.jpg");
        film35.setReleaseDate("1993-08-06");
        film35.setTitle("The Fugitive");
        film35.setVoteAverage(7.8);
        film35.setVoteCount(250000);

        Film film36 = new Film();
        film36.setId(933260);
        film36.setOverview("A young woman is plunged into a brutal dystopian future where she must battle her way to freedom.");
        film36.setOriginalTitle("The Substance");
        film36.setPosterPath("https://image.tmdb.org/t/p/w500/cGm2qnmXx9tFabmzEIkJZjCJdQd.jpg");
        film36.setReleaseDate("2012-03-23");
        film36.setTitle("The Hunger Games");
        film36.setVoteAverage(7.2);
        film36.setVoteCount(600000);

        Film film37 = new Film();
        film37.setId(744857);
        film37.setOverview("A space explorer and his ragtag crew fight off an invading alien force bent on destroying Earth.");
        film37.setOriginalTitle("Cuando acecha la maldad");
        film37.setPosterPath("https://image.tmdb.org/t/p/w500/iQ7G9LhP7NRRIUM4Vlai3eOxBAc.jpg");
        film37.setReleaseDate("2015-12-18");
        film37.setTitle("Star Wars: Episode VII - The Force Awakens");
        film37.setVoteAverage(7.8);
        film37.setVoteCount(700000);

        Film film38 = new Film();
        film38.setId(14784);
        film38.setOverview("A mysterious hero with supernatural abilities helps a group of teenagers defeat a terrifying threat.");
        film38.setOriginalTitle("The Fall");
        film38.setPosterPath("https://image.tmdb.org/t/p/w500/g3RKh7Gp2lDUnXQ0ZXq7xpdwA9e.jpg");
        film38.setReleaseDate("2014-10-07");
        film38.setTitle("The Flash");
        film38.setVoteAverage(7.0);
        film38.setVoteCount(30000);

        Film film39 = new Film();
        film39.setId(930564);
        film39.setOverview("A superhero must confront a deadly villain who threatens to destroy the world.");
        film39.setOriginalTitle("Saltburn");
        film39.setPosterPath("https://image.tmdb.org/t/p/w500/zGTfMwG112BC66mpaveVxoWPOaB.jpg");
        film39.setReleaseDate("2002-05-03");
        film39.setTitle("Spider-Man");
        film39.setVoteAverage(7.4);
        film39.setVoteCount(600000);

        Film film40 = new Film();
        film40.setId(1105832);
        film40.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film40.setOriginalTitle("Simón");
        film40.setPosterPath("https://image.tmdb.org/t/p/w500/9ltmI5Mef0NHWm4Nxcpm6hT8VhY.jpg");
        film40.setReleaseDate("2001-11-10");
        film40.setTitle("Harry Potter and the Sorcerer's Stone");
        film40.setVoteAverage(7.9);
        film40.setVoteCount(800000);

        // David Lynch
        Film film41 = new Film();
        film41.setId(1955);
        film41.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film41.setOriginalTitle("The Elephant Man");
        film41.setPosterPath("https://image.tmdb.org/t/p/w500/rk2lKgEtjF9HO9N2UFMRc2cMGdj.jpg");
        film41.setReleaseDate("2001-11-10");
        film41.setTitle("Harry Potter and the Sorcerer's Stone");
        film41.setVoteAverage(7.9);
        film41.setVoteCount(800000);

        Film film42 = new Film();
        film42.setId(841);
        film42.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film42.setOriginalTitle("Dune (Lynch)");
        film42.setPosterPath("https://image.tmdb.org/t/p/w500/4U8SybbIccwhTEZMs4KT7qefN7N.jpg");
        film42.setReleaseDate("2001-11-10");
        film42.setTitle("Harry Potter and the Sorcerer's Stone");
        film42.setVoteAverage(7.9);
        film42.setVoteCount(800000);

        Film film43 = new Film();
        film43.setId(1923);
        film43.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film43.setOriginalTitle("Twin Peaks: Fire Walk with Me");
        film43.setPosterPath("https://image.tmdb.org/t/p/w500/rol5H6loAojd6tH2VXQYEXzGQk1.jpg");
        film43.setReleaseDate("2001-11-10");
        film43.setTitle("Harry Potter and the Sorcerer's Stone");
        film43.setVoteAverage(7.9);
        film43.setVoteCount(800000);

        Film film44 = new Film();
        film44.setId(483);
        film44.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film44.setOriginalTitle("Wild at Heart");
        film44.setPosterPath("https://image.tmdb.org/t/p/w500/uLUFI5sJIfWrBUWB2Y1dEuyvvVy.jpg");
        film44.setReleaseDate("2001-11-10");
        film44.setTitle("Harry Potter and the Sorcerer's Stone");
        film44.setVoteAverage(7.9);
        film44.setVoteCount(800000);

        Film film45 = new Film();
        film45.setId(638);
        film45.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film45.setOriginalTitle("Lost Highway");
        film45.setPosterPath("https://image.tmdb.org/t/p/w500/fdTtij6H0sX9AzIjUeynh5zbfm7.jpg");
        film45.setReleaseDate("2001-11-10");
        film45.setTitle("Harry Potter and the Sorcerer's Stone");
        film45.setVoteAverage(7.9);
        film45.setVoteCount(800000);

        Film film46 = new Film();
        film46.setId(1730);
        film46.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film46.setOriginalTitle("Inland Empire");
        film46.setPosterPath("https://image.tmdb.org/t/p/w500/tAHMvvgWg7bNNHMCSND0diRjOtI.jpg");
        film46.setReleaseDate("2001-11-10");
        film46.setTitle("Harry Potter and the Sorcerer's Stone");
        film46.setVoteAverage(7.9);
        film46.setVoteCount(800000);

        // Míticas

        Film film47 = new Film();
        film47.setId(28);
        film47.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film47.setOriginalTitle("Apocalypse Now");
        film47.setPosterPath("https://image.tmdb.org/t/p/w500/gQB8Y5RCMkv2zwzFHbUJX3kAhvA.jpg");
        film47.setReleaseDate("2001-11-10");
        film47.setTitle("Harry Potter and the Sorcerer's Stone");
        film47.setVoteAverage(7.9);
        film47.setVoteCount(800000);

        Film film48 = new Film();
        film48.setId(238);
        film48.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film48.setOriginalTitle("The Godfather");
        film48.setPosterPath("https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg");
        film48.setReleaseDate("2001-11-10");
        film48.setTitle("Harry Potter and the Sorcerer's Stone");
        film48.setVoteAverage(7.9);
        film48.setVoteCount(800000);

        Film film49 = new Film();
        film49.setId(111);
        film49.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film49.setOriginalTitle("Scarface");
        film49.setPosterPath("https://image.tmdb.org/t/p/w500/iQ5ztdjvteGeboxtmRdXEChJOHh.jpg");
        film49.setReleaseDate("2001-11-10");
        film49.setTitle("Harry Potter and the Sorcerer's Stone");
        film49.setVoteAverage(7.9);
        film49.setVoteCount(800000);

        Film film50 = new Film();
        film50.setId(9552);
        film50.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film50.setOriginalTitle("The Exorcist");
        film50.setPosterPath("https://image.tmdb.org/t/p/w500/5x0CeVHJI8tcDx8tUUwYHQSNILq.jpg");
        film50.setReleaseDate("2001-11-10");
        film50.setTitle("Harry Potter and the Sorcerer's Stone");
        film50.setVoteAverage(7.9);
        film50.setVoteCount(800000);

        Film film51 = new Film();
        film51.setId(627);
        film51.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film51.setOriginalTitle("Trainspotting");
        film51.setPosterPath("https://image.tmdb.org/t/p/w500/1jUC02qsqS2NxBMFarbIhcQtazV.jpg");
        film51.setReleaseDate("2001-11-10");
        film51.setTitle("Harry Potter and the Sorcerer's Stone");
        film51.setVoteAverage(7.9);
        film51.setVoteCount(800000);

        Film film52 = new Film();
        film52.setId(603);
        film52.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film52.setOriginalTitle("Matrix");
        film52.setPosterPath("https://image.tmdb.org/t/p/w500/dXNAPwY7VrqMAo51EKhhCJfaGb5.jpg");
        film52.setReleaseDate("2001-11-10");
        film52.setTitle("Harry Potter and the Sorcerer's Stone");
        film52.setVoteAverage(7.9);
        film52.setVoteCount(800000);

        Film film53 = new Film();
        film53.setId(9298);
        film53.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film53.setOriginalTitle("Ali G Indahouse");
        film53.setPosterPath("https://image.tmdb.org/t/p/w500/pjKvQrVpekshVM6DUDOt3ImhAVH.jpg");
        film53.setReleaseDate("2001-11-10");
        film53.setTitle("Harry Potter and the Sorcerer's Stone");
        film53.setVoteAverage(7.9);
        film53.setVoteCount(800000);

        Film film54 = new Film();
        film54.setId(496);
        film54.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film54.setOriginalTitle("Borat");
        film54.setPosterPath("https://image.tmdb.org/t/p/w500/kfkyALfD4G1mlBJI1lOt2QCra4i.jpg");
        film54.setReleaseDate("2001-11-10");
        film54.setTitle("Harry Potter and the Sorcerer's Stone");
        film54.setVoteAverage(7.9);
        film54.setVoteCount(800000);

        Film film55 = new Film();
        film55.setId(539);
        film55.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film55.setOriginalTitle("Pyscho");
        film55.setPosterPath("https://image.tmdb.org/t/p/w500/yz4QVqPx3h1hD1DfqqQkCq3rmxW.jpg");
        film55.setReleaseDate("2001-11-10");
        film55.setTitle("Harry Potter and the Sorcerer's Stone");
        film55.setVoteAverage(7.9);
        film55.setVoteCount(800000);

        List<FilmList> filmLists = new ArrayList<>();
        FilmList list = new FilmList();
        FilmList list2 = new FilmList();
        FilmList list3 = new FilmList();
        FilmList list4 = new FilmList();
        FilmList list5 = new FilmList();
        FilmList list6 = new FilmList();

        list.setListName("⭐ Favoritas");
        list2.setListName("\uD83C\uDDEF\uD83C\uDDF5 Anime");
        list3.setListName("\uD83D\uDD5B Recientes");
        list4.setListName("\uD83D\uDC40 Pendientes");
        list5.setListName("❤\uFE0F David Lynch");
        list6.setListName("\uD83D\uDCFD\uFE0F Míticas");

        list.setContent(Arrays.asList(film1, film2, film3, film4, film5, film6, film7, film8, film9));
        list2.setContent(Arrays.asList(film10, film11, film12, film13, film14, film15, film16, film17, film18, film19));
        list3.setContent(Arrays.asList(film20, film21, film22, film23, film24, film25, film26, film27, film28, film29, film30));
        list4.setContent(Arrays.asList(film31, film32, film33, film34, film35, film36, film37, film38, film39, film40));
        list5.setContent(Arrays.asList(film41, film42, film43, film44, film45, film46));
        list6.setContent(Arrays.asList(film47, film48, film49, film50, film51, film52, film53, film54, film55));
        filmLists.add(list);
        filmLists.add(list2);
        filmLists.add(list3);
        filmLists.add(list4);
        filmLists.add(list5);
        filmLists.add(list6);
        return filmLists;
    }
}
