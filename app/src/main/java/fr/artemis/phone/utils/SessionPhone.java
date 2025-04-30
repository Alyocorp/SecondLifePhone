package fr.artemis.phone.utils;

import java.time.LocalDate;

import fr.artemis.phone.dto.SalarieLightDTO;
import fr.artemis.phone.dto.UtilisateurDTO;

/**
 * Session de l'utilisateur dans l'application
 */
public class SessionPhone {

    // Singleton
    private static SessionPhone instance = null;

    // L'utilisateur authentifié
    private UtilisateurDTO userDto = null;

    // Utilisateur authentifié ou non
    private boolean authenticated = false;

    // Flag indiquant si on est en cours de developpement ou en production
    private boolean devMode = false;

    // Etat de la connection Internet
    private boolean connected;

    // Test si la connexion est sur le réseau local ArtEmis
    private boolean artemisConnection = false;

    // Repertoire de stockage dans le mobile
    private String filePath;

    private LocalDate dateOuvertureSession = null;

    // Le salarie de la session
    private SalarieLightDTO salarie;

    // Dimensions de l'ecran du mobile
    private int screenWidth;
    private int screenHeight;
    
    private SessionPhone() {
    }

    public static SessionPhone getInstance() {
        if (null == instance) {
            instance = new SessionPhone();
        }
        return instance;
    }

    public void setUserDto(UtilisateurDTO userDto) {
        this.userDto = userDto;
    }

    public UtilisateurDTO getUserDto() {
        return userDto;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public SalarieLightDTO getSalarie() {
        return salarie;
    }

    public void setSalarie(SalarieLightDTO salarie) {
        this.salarie = salarie;
    }

    public void setArtemisConnection(boolean artemisConnection) {
        this.artemisConnection = artemisConnection;
    }

    public boolean isArtemisConnection() {
        return artemisConnection;
    }

    public LocalDate getDateOuvertureSession() {
        return dateOuvertureSession;
    }

    public void setDateOuvertureSession(LocalDate dateOuvertureSession) {
        this.dateOuvertureSession = dateOuvertureSession;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
    }
}