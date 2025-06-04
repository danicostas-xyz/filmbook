package xyz.danicostas.filmapp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xyz.danicostas.filmapp.R;
import xyz.danicostas.filmapp.model.Message;
import xyz.danicostas.filmapp.model.service.UserSession;
import xyz.danicostas.filmapp.view.adapter.FriendListAdapter;
import xyz.danicostas.filmapp.view.adapter.MessageAdapter;
import xyz.danicostas.filmapp.view.fragment.FriendsFragment;

/**
 * Actividad principal del chat que maneja tanto conversaciones con lista de amigos como con el chatbot FilmBot.
 * - Interfaz de chat con RecyclerView
 * - Soporte para mensajes enviados y recibidos
 * - Persistencia de mensajes usando SharedPreferences
 * - Funcionalidad especial para FilmBot con respuestas automáticas
 */
public class FriendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private EditText messageInput;
    private Button sendButton;
    private String friendName;
    private String friendProfileUrl;
    private static final String TAG = "FriendActivity";

    // Lista de mensajes y estado del chat
    private List<Message> messageList = new ArrayList<>();
    private boolean isChatbot = false;
    private OkHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);


        client = new OkHttpClient();

        // Configuración de la cabecera del chat
        TextView tvFriendName = findViewById(R.id.tvFriendNameDetail);
        friendName = getIntent().getStringExtra(FriendListAdapter.FRIEND_NAME);
        friendProfileUrl = getIntent().getStringExtra(FriendListAdapter.FRIEND_PROFILE_URL);
        tvFriendName.setText(friendName);

        // Ver  si es una conversación con FilmBot
        isChatbot = "FilmBot 🤖".equals(friendName);

        // Mostrar estado especial para FilmBot
        TextView tvFriendStatus = findViewById(R.id.tvFriendStatus);
        if (isChatbot) {
            tvFriendStatus.setText("AI Assistant - Always online");
        }

        // Configuración y carga de la foto de perfil
        ImageView ivFriendProfile = findViewById(R.id.ivFriendProfile);
        try {
            if (friendProfileUrl != null && !friendProfileUrl.isEmpty()) {
                if (friendProfileUrl.matches("\\d+")) {
                    int resourceId = Integer.parseInt(friendProfileUrl);
                    ivFriendProfile.setImageResource(resourceId);
                } else {
                    // Si es una URL
                    Glide.with(this)
                            .load(friendProfileUrl)
                            .placeholder(R.drawable.default_avatar_vector)
                            .error(R.drawable.default_avatar_vector)
                            .centerCrop()
                            .into(ivFriendProfile);
                }
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
            ivFriendProfile.setImageResource(R.drawable.default_avatar_vector);
        }

        // Configuración del botón de retroceso
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            saveMessages();
            finish();
        });

        // Cargar mensajes anteriores y configurar RecyclerView
        loadMessages();
        recyclerView = findViewById(R.id.recyclerViewMessages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Scroll automático hacia abajo
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter();
        recyclerView.setAdapter(messageAdapter);

        // Cargar mensajes existentes en el adaptador
        for (Message message : messageList) {
            messageAdapter.addMessage(message);
        }

        // Mensaje de bienvenida para FilmBot si nunca se ha hablado con el/ella :'v
        if (isChatbot && messageList.isEmpty()) {
            Message welcomeMessage = new Message("🎬 Hola Soy FilmBot, tu asistente de películas. ¿Quieres recomendaciones o hablar sobre cine?", Message.TYPE_RECEIVED);
            messageAdapter.addMessage(welcomeMessage);
            messageList.add(welcomeMessage);
            saveMessages();
        }

        // Configuración del input y botón de envío
        messageInput = findViewById(R.id.etMessageInput);
        sendButton = findViewById(R.id.btnSendMessage);
        sendButton.setOnClickListener(v -> sendMessage());
    }

    // Guardar mensajes al pausar la actividad
    @Override
    protected void onPause() {
        super.onPause();
        saveMessages();
    }

    /**
     * Maneja el envío de nuevos mensajes
     * - Valida que no esté vacío
     * - Añade el mensaje a la lista
     * - Si es FilmBot, genera respuesta
     */
    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            Message sentMessage = new Message(messageText, Message.TYPE_SENT);
            messageAdapter.addMessage(sentMessage);
            messageList.add(sentMessage);

            messageInput.setText("");
            scrollToBottom();
            saveMessages();

            if (isChatbot) {
                getChatbotResponse(messageText);
            }
        }
    }

    /**
     * Genera y muestra la respuesta del FilmBot
     * - Muestra indicador de escribiendo
     * - Simula delay de procesamiento
     * - Genera respuesta random
     */
    private void getChatbotResponse(String userMessage) {
        runOnUiThread(() -> {

            Message typingMessage = new Message("🤖 Escribiendo...", Message.TYPE_RECEIVED);
            messageAdapter.addMessage(typingMessage);
            scrollToBottom();

            // Simular pensamiento de IA
            new android.os.Handler().postDelayed(() -> {
                // Remover mensaje de escribiendo
                if (messageAdapter.getItemCount() > 0) {
                    messageAdapter.removeLastMessage();
                }

                String botResponse = generateIntelligentCinemaResponse(userMessage.toLowerCase());
                Message botMessage = new Message("🤖 " + botResponse, Message.TYPE_RECEIVED);
                messageAdapter.addMessage(botMessage);
                messageList.add(botMessage);
                scrollToBottom();
                saveMessages();
            }, 1800);
        });
    }

    // Respuestas como si fuera una IA
    private String generateIntelligentCinemaResponse(String userMessage) {

        if (userMessage.matches(".*\\b(hola|hey|saludos|buenas|que tal|como estas)\\b.*")) {
            String[] greetings = {
                    "¡Hola! Soy FilmBot, tu experto en cine. ¿Qué película te apetece descubrir hoy?",
                    "¡Hey, cinéfilo! ¿Listo para explorar el mundo del cine?",
                    "¡Saludos! Estoy aquí para hablar de películas. ¿Algún género en mente?",
                    "¡Buenas! Como tu asistente cinematográfico, ¿en qué puedo ayudarte?",
                    "¡Hola! ¿Buscas recomendaciones o quieres charlar sobre cine?"
            };
            return greetings[(int) (Math.random() * greetings.length)];
        }


        if (userMessage.matches(".*\\b(recomend|suger|aconsej|que.*ver|que.*miro)\\b.*")) {
            // Terror
            if (userMessage.contains("terror") || userMessage.contains("miedo")) {
                String[] horror = {
                        "Para terror te recomiendo: 'Hereditary' - terror psicológico brutal",
                        "Si buscas sustos: 'El Conjuro' o 'Insidious' son perfectas",
                        "Terror moderno: 'Midsommar' te dejará perturbado por días",
                        "'The Babadook' - terror australiano que es una obra maestra"
                };
                return horror[(int) (Math.random() * horror.length)];
            } else if (userMessage.contains("comedia") || userMessage.contains("risa")) {
                String[] comedy = {
                        "Comedia: 'El Gran Hotel Budapest' de Wes Anderson es genial",
                        "Para reír: 'Superbad' o '¿Qué pasó ayer?' son clásicos",
                        "Comedia romántica: 'Loco, Estúpido, Amor' es perfecta",
                        "'The Nice Guys' - comedia de acción con Ryan Gosling"
                };
                return comedy[(int) (Math.random() * comedy.length)];
            } else if (userMessage.contains("accion") || userMessage.contains("acción")) {
                String[] action = {
                        "Acción pura: 'Mad Max: Fury Road' es espectacular",
                        "Clásico moderno: 'John Wick' redefinió el género",
                        "Épica: 'Blade Runner 2049' es acción + ciencia ficción",
                        "'Casino Royale' - el mejor Bond de Daniel Craig"
                };
                return action[(int) (Math.random() * action.length)];
            } else {
                String[] general = {
                        "Te recomiendo 'Parasite' - drama coreano que ganó el Oscar",
                        "'Dune' (2021) es épica si te gusta la ciencia ficción",
                        "'Knives Out' - misterio moderno con gran elenco",
                        "'Everything Everywhere All at Once' es locura pura y genial"
                };
                return general[(int) (Math.random() * general.length)];
            }
        }


        if (userMessage.contains("género") || userMessage.contains("tipo") || userMessage.contains("categoria")) {
            return "Los géneros principales son: Drama, Comedia, Terror, Acción, Sci-Fi, Thriller, Romance, Animación. ¿Cuál te llama más la atención?";
        }


        if (userMessage.matches(".*\\b(actor|actriz|actuaci|interprete)\\b.*")) {
            String[] actorTalk = {
                    "¡Los actores son el alma del cine! ¿Tienes algún favorito en mente?",
                    "La actuación puede hacer o romper una película. ¿Qué interpretación te marcó?",
                    "Desde DiCaprio hasta Meryl Streep, hay tanto talento. ¿De quién quieres saber?",
                    "Una buena actuación te hace olvidar que estás viendo una película"
            };
            return actorTalk[(int) (Math.random() * actorTalk.length)];
        }

        if (userMessage.matches(".*\\b(director|dirigir|realizad)\\b.*")) {
            String[] directorTalk = {
                    "Los directores son los visionarios del cine. ¿Christopher Nolan, Tarantino, o Scorsese?",
                    "Cada director tiene su sello único. ¿Cuál admiras más?",
                    "De Kubrick a Denis Villeneuve, la dirección es arte puro",
                    "Un gran director puede hacer magia con cualquier historia"
            };
            return directorTalk[(int) (Math.random() * directorTalk.length)];
        }


        if (userMessage.matches(".*\\b(terror|miedo|susto|horror)\\b.*")) {
            String[] horror = {
                    "El terror es mi género favorito. ¿Prefieres psicológico o gore?",
                    "'The Shining' sigue siendo aterradora después de décadas",
                    "Terror moderno: 'It Follows' tiene un concepto brillante",
                    "¿Has visto 'Get Out'? Terror social de Jordan Peele es genial"
            };
            return horror[(int) (Math.random() * horror.length)];
        }


        if (userMessage.matches(".*\\b(marvel|dc|superhér|batman|superman|spider|avengers)\\b.*")) {
            String[] superhero = {
                    "¡Los superhéroes dominan el cine! ¿Team Marvel o Team DC?",
                    "'The Dark Knight' sigue siendo la mejor película de superhéroes",
                    "Marvel construyó algo épico con el MCU. ¿Viste Endgame?",
                    "DC tiene potencial: 'Wonder Woman' y 'Aquaman' fueron geniales"
            };
            return superhero[(int) (Math.random() * superhero.length)];
        }


        if (userMessage.matches(".*\\b(clasic|antiguas|viejo|moderno|actual|nuevo)\\b.*")) {
            String[] classics = {
                    "Los clásicos son atemporales: 'Casablanca', 'El Padrino'...",
                    "El cine moderno tiene efectos, pero ¿tiene el alma de los clásicos?",
                    "Hitchcock inventó el suspense. Scorsese lo perfeccionó",
                    "Cada época tiene sus joyas. ¿Prefieres historias de antes o actuales?"
            };
            return classics[(int) (Math.random() * classics.length)];
        }


        if (userMessage.matches(".*\\b(netflix|streaming|disney|amazon|hbo)\\b.*")) {
            String[] streaming = {
                    "¡Las plataformas cambiaron todo! Netflix produce contenido increíble",
                    "Disney+ tiene todo Marvel y Star Wars. ¿Ya viste The Mandalorian?",
                    "HBO Max: 'Game of Thrones', 'Succession'... contenido de calidad",
                    "El streaming nos dio acceso a cine mundial. ¡Aprovéchalo!"
            };
            return streaming[(int) (Math.random() * streaming.length)];
        }


        if (userMessage.matches(".*\\b(oscar|premio|award|ganar|mejor)\\b.*")) {
            return "Los Oscars son el máximo reconocimiento. ¿Cuál crees que debería haber ganado este año? Siempre hay controversias interesantes.";
        }


        String[] intelligent = {
                "Interesante punto sobre el cine. ¿Has notado cómo las películas reflejan su época?",
                "El cine es el arte más completo: imagen, sonido, narrativa. ¿Qué te parece más importante?",
                "Cada película es un mundo nuevo. ¿Prefieres escapismo o realismo?",
                "¿Sabías que el cine influye en la cultura más que cualquier otro medio?",
                "Una buena película te cambia. ¿Cuál fue la última que te impactó?",
                "El cine une a las personas. ¿Vas solo al cine o prefieres compañía?",
                "¿Te fijas en la cinematografía o solo en la historia?",
                "Los efectos especiales vs. la narrativa: ¿qué pesa más para ti?"
        };

        return intelligent[(int) (Math.random() * intelligent.length)];
    }

    /**
     * Hace scroll al último mensaje del chat
     */
    private void scrollToBottom() {
        if (messageAdapter.getItemCount() > 0) {
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        }
    }

    /**
     * Guarda los mensajes en SharedPreferences
     * Usa una clave única por usuario y conversación
     */
    private void saveMessages() {
        SharedPreferences sharedPreferences = getSharedPreferences("ChatMessages", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convertir mensajes a JSON
        Gson gson = new Gson();
        String json = gson.toJson(messageList);

        // Crear clave única para cada chat
        String currentUserId = UserSession.getInstance().getUserId();
        if (currentUserId == null || currentUserId.isEmpty()) {
            currentUserId = "anonymous_user";
        }

        String key = "messages_" + currentUserId + "_" + friendName.replaceAll("\\s+", "_").toLowerCase();
        editor.putString(key, json);
        editor.apply();

        Log.d(TAG, "Messages saved for user: " + currentUserId + " with friend: " + friendName);
    }

    /**
     * Carga mensajes previos desde SharedPreferences
     * Usa la misma clave única que saveMessages()
     */
    private void loadMessages() {
        SharedPreferences sharedPreferences = getSharedPreferences("ChatMessages", Context.MODE_PRIVATE);

        String currentUserId = UserSession.getInstance().getUserId();
        if (currentUserId == null || currentUserId.isEmpty()) {
            currentUserId = "anonymous_user";
        }

        String key = "messages_" + currentUserId + "_" + friendName.replaceAll("\\s+", "_").toLowerCase();
        String json = sharedPreferences.getString(key, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Message>>(){}.getType();
            messageList = gson.fromJson(json, type);

            if (messageList == null) {
                messageList = new ArrayList<>();
            }

            Log.d(TAG, "Loaded " + messageList.size() + " messages for user: " + currentUserId + " with friend: " + friendName);
        } else {
            Log.d(TAG, "No mensajes guardados para el usuario: " + currentUserId + " con " + friendName);
            messageList = new ArrayList<>();
        }
    }
}
