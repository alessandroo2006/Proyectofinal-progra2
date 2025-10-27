package com.example.prueba;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class AvatarView extends FrameLayout {

    private ImageView ivBase;
    private ImageView ivShirt;
    private ImageView ivHair;
    private ImageView ivAccessory;

    public AvatarView(Context context) {
        super(context);
        init();
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Crear las ImageViews superpuestas
        ivBase = new ImageView(getContext());
        ivShirt = new ImageView(getContext());
        ivHair = new ImageView(getContext());
        ivAccessory = new ImageView(getContext());

        // Configurar las ImageViews
        setupImageView(ivBase);
        setupImageView(ivShirt);
        setupImageView(ivHair);
        setupImageView(ivAccessory);

        // Agregar las vistas en orden (de abajo a arriba)
        addView(ivBase);
        addView(ivShirt);
        addView(ivHair);
        addView(ivAccessory);

        // Configuración inicial
        ivBase.setImageResource(R.drawable.avatar_base);
        ivShirt.setVisibility(GONE);
        ivHair.setVisibility(GONE);
        ivAccessory.setVisibility(GONE);
    }

    private void setupImageView(ImageView imageView) {
        LayoutParams params = new LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        );
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    /**
     * Establece el peinado del avatar
     * @param hairId ID del recurso drawable (ej: "hair_default", "hair_01")
     */
    public void setHair(String hairId) {
        if (hairId == null || hairId.equals("none")) {
            ivHair.setVisibility(GONE);
        } else {
            int resourceId = getContext().getResources().getIdentifier(
                hairId, "drawable", getContext().getPackageName()
            );
            if (resourceId != 0) {
                ivHair.setImageResource(resourceId);
                ivHair.setVisibility(VISIBLE);
            } else {
                // Si no existe el recurso, usar un placeholder de color
                ivHair.setBackgroundColor(getHairColor(hairId));
                ivHair.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * Establece la camiseta del avatar
     * @param shirtId ID del recurso drawable (ej: "shirt_default", "shirt_01")
     */
    public void setShirt(String shirtId) {
        if (shirtId == null || shirtId.equals("none")) {
            ivShirt.setVisibility(GONE);
        } else {
            int resourceId = getContext().getResources().getIdentifier(
                shirtId, "drawable", getContext().getPackageName()
            );
            if (resourceId != 0) {
                ivShirt.setImageResource(resourceId);
                ivShirt.setVisibility(VISIBLE);
            } else {
                // Si no existe el recurso, usar un placeholder de color
                ivShirt.setBackgroundColor(getShirtColor(shirtId));
                ivShirt.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * Establece el accesorio del avatar
     * @param accessoryId ID del recurso drawable (ej: "accessory_sunglasses") o "none"
     */
    public void setAccessory(String accessoryId) {
        if (accessoryId == null || accessoryId.equals("none")) {
            ivAccessory.setVisibility(GONE);
        } else {
            int resourceId = getContext().getResources().getIdentifier(
                accessoryId, "drawable", getContext().getPackageName()
            );
            if (resourceId != 0) {
                ivAccessory.setImageResource(resourceId);
                ivAccessory.setVisibility(VISIBLE);
            } else {
                // Si no existe el recurso, usar un placeholder de color
                ivAccessory.setBackgroundColor(getAccessoryColor(accessoryId));
                ivAccessory.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * Aplica la configuración por defecto del avatar
     */
    public void setDefaultConfiguration() {
        setHair("hair_default");
        setShirt("shirt_default");
        setAccessory("none");
    }

    /**
     * Limpia todo el avatar (solo muestra la base)
     */
    public void clearAvatar() {
        ivShirt.setVisibility(GONE);
        ivHair.setVisibility(GONE);
        ivAccessory.setVisibility(GONE);
    }

    /**
     * Obtiene el color placeholder para el peinado
     */
    private int getHairColor(String hairId) {
        switch (hairId) {
            case "hair_default":
                return 0xFF8B4513; // Marrón
            case "hair_01":
                return 0xFF0000FF; // Azul
            default:
                return 0xFF000000; // Negro
        }
    }

    /**
     * Obtiene el color placeholder para la camiseta
     */
    private int getShirtColor(String shirtId) {
        switch (shirtId) {
            case "shirt_default":
                return 0xFFFFFFFF; // Blanco
            case "shirt_01":
                return 0xFFFF0000; // Rojo
            default:
                return 0xFF808080; // Gris
        }
    }

    /**
     * Obtiene el color placeholder para el accesorio
     */
    private int getAccessoryColor(String accessoryId) {
        switch (accessoryId) {
            case "accessory_sunglasses":
                return 0xFF000000; // Negro
            default:
                return 0xFF666666; // Gris oscuro
        }
    }
}
