package xyz.danicostas.filmapp.model.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.entity.Film;
import xyz.danicostas.filmapp.model.entity.FilmList;
import xyz.danicostas.filmapp.model.entity.Review;
import xyz.danicostas.filmapp.model.entity.User;
import xyz.danicostas.filmapp.model.persistence.DaoUser;
import xyz.danicostas.filmapp.view.activity.ApplicationActivity;
import xyz.danicostas.filmapp.view.activity.LoginActivity;
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

    public void login(Context context, String email, String password) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);

        final AlertDialog progressDialog = new AlertDialog.Builder(context)
            .setView(dialogView)
            .create();
        progressDialog.show();

        if (mAuth != null) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        userService.getUserData(mAuth.getCurrentUser().getUid());
                        Intent intent = new Intent(context, ApplicationActivity.class);
                        context.startActivity(intent);

                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Invalid Credentials";
                        Log.e("GestorUser", "Login failed: " + errorMessage);
                        Toast.makeText(context, context.getString(R.string.loginKO) + " " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
        } else {
            Log.e("GestorUser", "FirebaseAuth is not initialized.");
            progressDialog.dismiss(); // Make sure to dismiss the dialog if mAuth is null
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
     * Aditionally, it also checks if the email is already in the Database.
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
                            user.setListOfReviews(new ArrayList<Review>());
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
                "review001",
                "user123",
                "Lynch en su máxima expresión",
                "Una pesadilla surrealista llena de simbolismo y atmósfera inquietante. Una obra maestra del cine experimental.",
                "/mxveW3mGVc0DzLdOmtkZsgd7c3B.jpg",
                "985",
                "Eraserhead",
                new Date(),
                9
        );

        Review review2 = new Review(
                "review002",
                "user456",
                "Una obra maestra del cine neo-noir",
                "Un retrato psicológico brutal de la alienación y la locura en una ciudad decadente. De Niro está impresionante.",
                "/ekstpH614fwDX8DUln1a2Opz0N8.jpg",
                "103",
                "Taxi Driver",
                new Date(),
                10
        );

        Review review3 = new Review(
                "review003",
                "user789",
                "El espíritu de la infancia en su máxima expresión",
                "Una historia encantadora, llena de magia y calidez. Perfecta para todas las edades.",
                "/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg",
                "8392",
                "Mi vecino Totoro",
                new Date(),
                8
        );

        return List.of(review1, review2, review3);

    }

    @NonNull
    private List<FilmList> mockListOfLists() {
        Film film1 = new Film();
        film1.setId(426063);
        film1.setOverview("A gothic tale of obsession between a haunted young woman and the terrifying vampire infatuated with her, causing untold horror in its wake.");
        film1.setOriginalTitle("Nosferatu");
        film1.setPosterPath("https://image.tmdb.org/t/p/w500/5qGIxdEO841C0tdY8vOdLoRVrr0.jpg");
        film1.setReleaseDate("2024-12-25");
        film1.setTitle("Nosferatu");
        film1.setVoteAverage(6.7);
        film1.setVoteCount(2002);

        Film film2 = new Film();
        film2.setId(19995);
        film2.setOverview("A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.");
        film2.setOriginalTitle("Avatar");
        film2.setPosterPath("https://image.tmdb.org/t/p/w500/kyeqWdyUXW608qlYkRqosgbbJyK.jpg");
        film2.setReleaseDate("2009-12-18");
        film2.setTitle("Avatar");
        film2.setVoteAverage(7.8);
        film2.setVoteCount(25000);

        Film film3 = new Film();
        film3.setId(550);
        film3.setOverview("An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.");
        film3.setOriginalTitle("Fight Club");
        film3.setPosterPath("https://image.tmdb.org/t/p/w500/bptfVGEQuv6vDTIMVCHjJ9Dz8PX.jpg");
        film3.setReleaseDate("1999-10-15");
        film3.setTitle("Fight Club");
        film3.setVoteAverage(8.8);
        film3.setVoteCount(24000);

        Film film4 = new Film();
        film4.setId(807);
        film4.setOverview("A twisted odyssey through the wreckage of post-industrial America.");
        film4.setOriginalTitle("Eraserhead");
        film4.setPosterPath("https://image.tmdb.org/t/p/w500/mxveW3mGVc0DzLdOmtkZsgd7c3B.jpg");
        film4.setReleaseDate("1977-03-19");
        film4.setTitle("Eraserhead");
        film4.setVoteAverage(7.5);
        film4.setVoteCount(1300);

        Film film5 = new Film();
        film5.setId(601);
        film5.setOverview("A group of high schoolers discover that their dreams are being invaded by a killer.");
        film5.setOriginalTitle("A Nightmare on Elm Street");
        film5.setPosterPath("https://image.tmdb.org/t/p/w500/wGTpGGRMZmyFCcrY2YoxVTIBlli.jpg");
        film5.setReleaseDate("1984-11-09");
        film5.setTitle("A Nightmare on Elm Street");
        film5.setVoteAverage(7.3);
        film5.setVoteCount(5000);

        Film film6 = new Film();
        film6.setId(299534);
        film6.setOverview("A thief and a group of strangers try to pull off a heist during a massive hurricane.");
        film6.setOriginalTitle("The Hurricane Heist");
        film6.setPosterPath("https://image.tmdb.org/t/p/w500/rAmcj5IZcx59dhev3UnVDEGlImK.jpg");
        film6.setReleaseDate("2018-03-09");
        film6.setTitle("The Hurricane Heist");
        film6.setVoteAverage(5.0);
        film6.setVoteCount(500);

        Film film7 = new Film();
        film7.setId(419704);
        film7.setOverview("A young woman is forced to team up with a criminal in order to survive a ruthless city.");
        film7.setOriginalTitle("Peppermint");
        film7.setPosterPath("https://image.tmdb.org/t/p/w500/jrzxS0vcbzIIay1sdYm0rgI2QfJ.jpg.jpg");
        film7.setReleaseDate("2018-09-07");
        film7.setTitle("Peppermint");
        film7.setVoteAverage(6.5);
        film7.setVoteCount(2000);

        Film film8 = new Film();
        film8.setId(536931);
        film8.setOverview("A disillusioned soldier faces difficult choices as a dangerous plot unravels in a futuristic dystopia.");
        film8.setOriginalTitle("Ready Player One");
        film8.setPosterPath("https://image.tmdb.org/t/p/w500/pU1ULUq8D3iRxl1fdX2lZIzdHuI.jpg");
        film8.setReleaseDate("2018-03-29");
        film8.setTitle("Ready Player One");
        film8.setVoteAverage(7.5);
        film8.setVoteCount(50000);

        Film film9 = new Film();
        film9.setId(324857);
        film9.setOverview("A team of scientists explore an uncharted planet, discovering dangerous wildlife and a race of mysterious humanoid creatures.");
        film9.setOriginalTitle("Alien: Covenant");
        film9.setPosterPath("https://image.tmdb.org/t/p/w500/zecMELPbU5YMQpC81Z8ImaaXuf9.jpg");
        film9.setReleaseDate("2017-05-12");
        film9.setTitle("Alien: Covenant");
        film9.setVoteAverage(6.4);
        film9.setVoteCount(25000);

        Film film10 = new Film();
        film10.setId(291803);
        film10.setOverview("A woman is forced to choose between saving her husband or saving the world.");
        film10.setOriginalTitle("The Maze Runner");
        film10.setPosterPath("https://image.tmdb.org/t/p/w500/ode14q7WtDugFDp78fo9lCsmay9.jpg");
        film10.setReleaseDate("2014-09-19");
        film10.setTitle("The Maze Runner");
        film10.setVoteAverage(6.8);
        film10.setVoteCount(45000);

        Film film11 = new Film();
        film11.setId(38323);
        film11.setOverview("In a dystopian future where books are banned, one fireman seeks to preserve knowledge.");
        film11.setOriginalTitle("Fahrenheit 451");
        film11.setPosterPath("https://image.tmdb.org/t/p/w500/k2CTpexoS9MO9lKVFfnzwVdJuM.jpg");
        film11.setReleaseDate("2018-05-19");
        film11.setTitle("Fahrenheit 451");
        film11.setVoteAverage(6.1);
        film11.setVoteCount(5000);

        Film film12 = new Film();
        film12.setId(148);
        film12.setOverview("A disillusioned writer and his wife live a quiet, isolated life in the countryside, where a series of strange occurrences test their sanity.");
        film12.setOriginalTitle("The Shining");
        film12.setPosterPath("https://image.tmdb.org/t/p/w500/fFYAlrOudDJRYs8tvuHbUk0OGdL.jpg");
        film12.setReleaseDate("1980-05-23");
        film12.setTitle("The Shining");
        film12.setVoteAverage(8.4);
        film12.setVoteCount(30000);

        Film film13 = new Film();
        film13.setId(312157);
        film13.setOverview("A young woman gets tangled in a web of conspiracy and deadly secrets while investigating the mysterious disappearance of her twin sister.");
        film13.setOriginalTitle("The Secret");
        film13.setPosterPath("https://image.tmdb.org/t/p/w500/zLcm3V8tjybWZVbE0MIwJfvmv0g.jpg");
        film13.setReleaseDate("2018-04-10");
        film13.setTitle("The Secret");
        film13.setVoteAverage(7.2);
        film13.setVoteCount(4000);

        Film film14 = new Film();
        film14.setId(120);
        film14.setOverview("A young woman becomes involved with a man whose troubled past brings danger into their lives.");
        film14.setOriginalTitle("The Godfather");
        film14.setPosterPath("https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg");
        film14.setReleaseDate("1972-03-24");
        film14.setTitle("The Godfather");
        film14.setVoteAverage(9.2);
        film14.setVoteCount(100000);

        Film film15 = new Film();
        film15.setId(8489);
        film15.setOverview("A group of kids in a small town discover that their neighbor is a vampire.");
        film15.setOriginalTitle("The Lost Boys");
        film15.setPosterPath("https://image.tmdb.org/t/p/w500/x9UH5kYuJRM0rmKV81XQjv55V3H.jpg");
        film15.setReleaseDate("1987-07-31");
        film15.setTitle("The Lost Boys");
        film15.setVoteAverage(7.3);
        film15.setVoteCount(15000);

        Film film16 = new Film();
        film16.setId(156);
        film16.setOverview("A group of children discover a magical land in a wardrobe and become involved in a battle between good and evil.");
        film16.setOriginalTitle("The Chronicles of Narnia: The Lion, the Witch and the Wardrobe");
        film16.setPosterPath("https://image.tmdb.org/t/p/w500/iREd0rNCjYdf5Ar0vfaW32yrkm.jpg");
        film16.setReleaseDate("2005-12-09");
        film16.setTitle("The Chronicles of Narnia: The Lion, the Witch and the Wardrobe");
        film16.setVoteAverage(7.1);
        film16.setVoteCount(30000);

        Film film17 = new Film();
        film17.setId(1089);
        film17.setOverview("A secret agent embarks on a mission to stop a dangerous international conspiracy.");
        film17.setOriginalTitle("Mission: Impossible");
        film17.setPosterPath("https://image.tmdb.org/t/p/w500/l5uxY5m5OInWpcExIpKG6AR3rgL.jpg");
        film17.setReleaseDate("1996-05-22");
        film17.setTitle("Mission: Impossible");
        film17.setVoteAverage(6.9);
        film17.setVoteCount(25000);

        Film film18 = new Film();
        film18.setId(313369);
        film18.setOverview("A group of astronauts gets trapped in space and must fight to survive.");
        film18.setOriginalTitle("Gravity");
        film18.setPosterPath("https://image.tmdb.org/t/p/w500/kZ2nZw8D681aphje8NJi8EfbL1U.jpg");
        film18.setReleaseDate("2013-10-04");
        film18.setTitle("Gravity");
        film18.setVoteAverage(7.7);
        film18.setVoteCount(60000);

        Film film19 = new Film();
        film19.setId(39149);
        film19.setOverview("A warrior fights to save his city from destruction by a deadly force of ancient enemies.");
        film19.setOriginalTitle("300");
        film19.setPosterPath("https://image.tmdb.org/t/p/w500/h7Lcio0c9ohxPhSZg42eTlKIVVY.jpg");
        film19.setReleaseDate("2006-03-09");
        film19.setTitle("300");
        film19.setVoteAverage(7.6);
        film19.setVoteCount(100000);

        Film film20 = new Film();
        film20.setId(4975);
        film20.setOverview("A war between two factions erupts after a devastating attack on a peaceful village.");
        film20.setOriginalTitle("Star Wars: Episode III - Revenge of the Sith");
        film20.setPosterPath("https://image.tmdb.org/t/p/w500/xfSAoBEm9MNBjmlNcDYLvLSMlnq.jpg");
        film20.setReleaseDate("2005-05-19");
        film20.setTitle("Star Wars: Episode III - Revenge of the Sith");
        film20.setVoteAverage(7.7);
        film20.setVoteCount(250000);

        Film film21 = new Film();
        film21.setId(24547);
        film21.setOverview("A young woman embarks on a mission to retrieve an ancient artifact that has the power to change the world.");
        film21.setOriginalTitle("The Tomb Raider");
        film21.setPosterPath("https://image.tmdb.org/t/p/w500/s4Qn5LF6OwK4rIifmthIDtbqDSs.jpg");
        film21.setReleaseDate("2018-03-16");
        film21.setTitle("Tomb Raider");
        film21.setVoteAverage(6.3);
        film21.setVoteCount(30000);

        Film film22 = new Film();
        film22.setId(158);
        film22.setOverview("The story of an unlikely hero who seeks to save humanity from a global extinction.");
        film22.setOriginalTitle("The Matrix");
        film22.setPosterPath("https://image.tmdb.org/t/p/w500/dXNAPwY7VrqMAo51EKhhCJfaGb5.jpg");
        film22.setReleaseDate("1999-03-31");
        film22.setTitle("The Matrix");
        film22.setVoteAverage(8.7);
        film22.setVoteCount(500000);

        Film film23 = new Film();
        film23.setId(793);
        film23.setOverview("A newlywed couple’s honeymoon turns into a nightmare as a vengeful ghost pursues them.");
        film23.setOriginalTitle("The Haunting of Bly Manor");
        film23.setPosterPath("https://image.tmdb.org/t/p/w500/7hlgzJXLgyECS1mk3LSN3E72l5N.jpg");
        film23.setReleaseDate("2020-10-09");
        film23.setTitle("Blue Velvet");
        film23.setVoteAverage(7.5);
        film23.setVoteCount(15000);

        Film film24 = new Film();
        film24.setId(76341);
        film24.setOverview("An unassuming hotel manager must fight off criminals who try to steal a precious jewel hidden in his hotel.");
        film24.setOriginalTitle("The Grand Budapest Hotel");
        film24.setPosterPath("https://image.tmdb.org/t/p/w500/lFcNo24Nr6z58w77ltjXJvCZm9R.jpg");
        film24.setReleaseDate("2014-03-28");
        film24.setTitle("The Grand Budapest Hotel");
        film24.setVoteAverage(8.1);
        film24.setVoteCount(40000);

        Film film25 = new Film();
        film25.setId(546554);
        film25.setOverview("A team of adventurers seeks to explore a lost city beneath the desert sands.");
        film25.setOriginalTitle("The Mummy");
        film25.setPosterPath("https://image.tmdb.org/t/p/w500/yhIsVvcUm7QxzLfT6HW2wLf5ajY.jpg");
        film25.setReleaseDate("2017-06-09");
        film25.setTitle("The Mummy");
        film25.setVoteAverage(6.1);
        film25.setVoteCount(25000);

        Film film26 = new Film();
        film26.setId(315635);
        film26.setOverview("A hero with supernatural abilities teams up with a group of warriors to protect a magical kingdom from evil forces.");
        film26.setOriginalTitle("Thor: Ragnarok");
        film26.setPosterPath("https://image.tmdb.org/t/p/w500/rzRwTcFvttcN1ZpX2xv4j3tSdJu.jpg");
        film26.setReleaseDate("2017-10-25");
        film26.setTitle("Thor: Ragnarok");
        film26.setVoteAverage(7.9);
        film26.setVoteCount(400000);

        Film film27 = new Film();
        film27.setId(774);
        film27.setOverview("A young girl embarks on a quest to defeat a terrifying dragon and save her village.");
        film27.setOriginalTitle("How to Train Your Dragon");
        film27.setPosterPath("https://image.tmdb.org/t/p/w500/47HoQdGvW4Jy4ntUbsQ3bEnJHDh.jpg");
        film27.setReleaseDate("2010-03-26");
        film27.setTitle("How to Train Your Dragon");
        film27.setVoteAverage(8.1);
        film27.setVoteCount(150000);

        Film film28 = new Film();
        film28.setId(667);
        film28.setOverview("A space captain and his crew are sent on a mission to explore new galaxies in search of new life.");
        film28.setOriginalTitle("Star Trek");
        film28.setPosterPath("https://image.tmdb.org/t/p/w500/sqiCinCzUGlzQiFytS30N1nO3Pt.jpg");
        film28.setReleaseDate("2009-05-08");
        film28.setTitle("Star Trek");
        film28.setVoteAverage(7.9);
        film28.setVoteCount(250000);

        Film film29 = new Film();
        film29.setId(39414);
        film29.setOverview("An elite squad of soldiers battles an ancient, mysterious force that threatens the fate of the world.");
        film29.setOriginalTitle("The Expendables");
        film29.setPosterPath("https://image.tmdb.org/t/p/w500/j09ZkH6R4JWVylBcDai1laCmGw7.jpg");
        film29.setReleaseDate("2010-08-13");
        film29.setTitle("The Expendables");
        film29.setVoteAverage(6.4);
        film29.setVoteCount(35000);

        Film film30 = new Film();
        film30.setId(306651);
        film30.setOverview("A criminal mastermind threatens to bring chaos to Gotham City, and it's up to Batman to stop him.");
        film30.setOriginalTitle("The Dark Knight");
        film30.setPosterPath("https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg");
        film30.setReleaseDate("2008-07-18");
        film30.setTitle("The Dark Knight");
        film30.setVoteAverage(8.8);
        film30.setVoteCount(120000);

        Film film31 = new Film();
        film31.setId(20677);
        film31.setOverview("A group of high school students must band together to survive a zombie apocalypse.");
        film31.setOriginalTitle("The Breakfast Club");
        film31.setPosterPath("https://image.tmdb.org/t/p/w500/wM9ErA8UVdcce5P4oefQinN8VVV.jpg");
        film31.setReleaseDate("1985-02-15");
        film31.setTitle("The Breakfast Club");
        film31.setVoteAverage(7.9);
        film31.setVoteCount(25000);

        Film film32 = new Film();
        film32.setId(10074);
        film32.setOverview("An orphaned young woman discovers her true identity while facing a dark, magical curse.");
        film32.setOriginalTitle("The Princess Bride");
        film32.setPosterPath("https://image.tmdb.org/t/p/w500/2FC9L9MrjBoGHYjYZjdWQdopVYb.jpg");
        film32.setReleaseDate("1987-09-25");
        film32.setTitle("The Princess Bride");
        film32.setVoteAverage(8.1);
        film32.setVoteCount(40000);

        Film film33 = new Film();
        film33.setId(2179);
        film33.setOverview("A group of misfit teenagers are forced to save the world from a vengeful alien.");
        film33.setOriginalTitle("Guardians of the Galaxy");
        film33.setPosterPath("https://image.tmdb.org/t/p/w500/jPrJPZKJVhvyJ4DmUTrDgmFN0yG.jpg");
        film33.setReleaseDate("2014-07-30");
        film33.setTitle("Guardians of the Galaxy");
        film33.setVoteAverage(7.9);
        film33.setVoteCount(650000);

        Film film34 = new Film();
        film34.setId(159);
        film34.setOverview("A detective investigates the mysterious death of a beautiful woman who may be the victim of a notorious serial killer.");
        film34.setOriginalTitle("Se7en");
        film34.setPosterPath("https://image.tmdb.org/t/p/w500/191nKfP0ehp3uIvWqgPbFmI4lv9.jpg");
        film34.setReleaseDate("1995-09-22");
        film34.setTitle("Se7en");
        film34.setVoteAverage(8.6);
        film34.setVoteCount(600000);

        Film film35 = new Film();
        film35.setId(166);
        film35.setOverview("A simple man’s life is turned upside down when he gets caught in a high-stakes conspiracy involving political corruption.");
        film35.setOriginalTitle("The Fugitive");
        film35.setPosterPath("https://image.tmdb.org/t/p/w500/b3rEtLKyOnF89mcK75GXDXdmOEf.jpg");
        film35.setReleaseDate("1993-08-06");
        film35.setTitle("The Fugitive");
        film35.setVoteAverage(7.8);
        film35.setVoteCount(250000);

        Film film36 = new Film();
        film36.setId(15867);
        film36.setOverview("A young woman is plunged into a brutal dystopian future where she must battle her way to freedom.");
        film36.setOriginalTitle("The Hunger Games");
        film36.setPosterPath("https://image.tmdb.org/t/p/w500/kFC9H1K1FeiWdbxayX4CskmWpS5.jpg");
        film36.setReleaseDate("2012-03-23");
        film36.setTitle("The Hunger Games");
        film36.setVoteAverage(7.2);
        film36.setVoteCount(600000);

        Film film37 = new Film();
        film37.setId(28510);
        film37.setOverview("A space explorer and his ragtag crew fight off an invading alien force bent on destroying Earth.");
        film37.setOriginalTitle("Star Wars: Episode VII - The Force Awakens");
        film37.setPosterPath("https://image.tmdb.org/t/p/w500/wqnLdwVXoBjKibFRR5U3y0aDUhs.jpg");
        film37.setReleaseDate("2015-12-18");
        film37.setTitle("Star Wars: Episode VII - The Force Awakens");
        film37.setVoteAverage(7.8);
        film37.setVoteCount(700000);

        Film film38 = new Film();
        film38.setId(22539);
        film38.setOverview("A mysterious hero with supernatural abilities helps a group of teenagers defeat a terrifying threat.");
        film38.setOriginalTitle("The Flash");
        film38.setPosterPath("https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg");
        film38.setReleaseDate("2014-10-07");
        film38.setTitle("The Flash");
        film38.setVoteAverage(7.0);
        film38.setVoteCount(30000);

        Film film39 = new Film();
        film39.setId(134);
        film39.setOverview("A superhero must confront a deadly villain who threatens to destroy the world.");
        film39.setOriginalTitle("Spider-Man");
        film39.setPosterPath("https://image.tmdb.org/t/p/w500/gh4cZbhZxyTbgxQPxD0dOudNPTn.jpg");
        film39.setReleaseDate("2002-05-03");
        film39.setTitle("Spider-Man");
        film39.setVoteAverage(7.4);
        film39.setVoteCount(600000);

        Film film40 = new Film();
        film40.setId(32929);
        film40.setOverview("A young boy discovers he has supernatural abilities and is trained to fight evil forces.");
        film40.setOriginalTitle("Harry Potter and the Sorcerer's Stone");
        film40.setPosterPath("https://image.tmdb.org/t/p/w500/wuMc08IPKEatf9rnMNXvIDxqP4W.jpg");
        film40.setReleaseDate("2001-11-10");
        film40.setTitle("Harry Potter and the Sorcerer's Stone");
        film40.setVoteAverage(7.9);
        film40.setVoteCount(800000);

        List<FilmList> filmLists = new ArrayList<>();
        FilmList list = new FilmList();
        FilmList list2 = new FilmList();
        FilmList list3 = new FilmList();
        FilmList list4 = new FilmList();
        FilmList list5 = new FilmList();
        list.setListName("⭐Favoritas");
        list2.setListName("\uD83C\uDDEF\uD83C\uDDF5Anime");
        list3.setListName("\uD83D\uDC40Pendientes");
        list4.setListName("❤\uFE0FDavid Lynch");
        list5.setListName("\uD83D\uDC80Terror");
        list.setContent(Arrays.asList(film1, film2, film3, film4, film5, film6, film7, film8));
        list2.setContent(Arrays.asList(film9, film10, film11, film12, film13, film14, film15, film16));
        list3.setContent(Arrays.asList(film17, film18, film19, film20, film21, film22, film23, film24));
        list4.setContent(Arrays.asList(film25, film26, film27, film28, film29, film30, film31, film32));
        list5.setContent(Arrays.asList(film33, film34, film35, film36, film37, film38, film39, film40));
        filmLists.add(list);
        filmLists.add(list2);
        filmLists.add(list3);
        filmLists.add(list5);
        return filmLists;
    }
}
