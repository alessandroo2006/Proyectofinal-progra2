package com.example.prueba;

import android.content.Context;
import android.os.AsyncTask;

public class UserRepository {
    private UserDao userDao;
    
    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        userDao = database.userDao();
    }
    
    public interface LoginCallback {
        void onLoginSuccess(User user);
        void onLoginFailure(String message);
    }
    
    public interface RegisterCallback {
        void onRegisterSuccess(User user);
        void onRegisterFailure(String message);
    }
    
    public void loginUser(String username, String password, LoginCallback callback) {
        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... voids) {
                User user = userDao.getUserByUsername(username);
                if (user != null && user.getPassword().equals(SecurityHelper.hashPassword(password))) {
                    return user;
                }
                return null;
            }
            
            @Override
            protected void onPostExecute(User user) {
                if (user != null) {
                    callback.onLoginSuccess(user);
                } else {
                    callback.onLoginFailure("Usuario o contraseña incorrectos");
                }
            }
        }.execute();
    }
    
    public void registerUser(String username, String password, String email, RegisterCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                // Verificar si el usuario ya existe
                if (userDao.checkUsernameExists(username) > 0) {
                    return "El nombre de usuario ya existe";
                }
                
                if (userDao.checkEmailExists(email) > 0) {
                    return "El email ya está registrado";
                }
                
                // Crear nuevo usuario
                User newUser = new User(username, password, email, ""); // fullName vacío por defecto
                newUser.setPasswordHashed(password); // Hash de la contraseña
                long userId = userDao.insertUser(newUser);
                
                if (userId > 0) {
                    newUser.setId((int) userId);
                    return "SUCCESS";
                } else {
                    return "Error al crear el usuario";
                }
            }
            
            @Override
            protected void onPostExecute(String result) {
                if (result.equals("SUCCESS")) {
                    User user = new User(username, password, email, ""); // fullName vacío por defecto
                    callback.onRegisterSuccess(user);
                } else {
                    callback.onRegisterFailure(result);
                }
            }
        }.execute();
    }
    
    public void checkUsernameExists(String username, UsernameCheckCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                return userDao.checkUsernameExists(username) > 0;
            }
            
            @Override
            protected void onPostExecute(Boolean exists) {
                callback.onUsernameCheck(exists);
            }
        }.execute();
    }
    
    public interface UsernameCheckCallback {
        void onUsernameCheck(boolean exists);
    }
    
    public User getUserByUsername(String username) {
        try {
            return userDao.getUserByUsername(username);
        } catch (Exception e) {
            return null;
        }
    }
}
