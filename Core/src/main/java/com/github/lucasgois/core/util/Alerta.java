package com.github.lucasgois.core.util;

import com.github.lucasgois.core.exceptions.AvisoException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Alerta {

    default void aviso(final String mensagem) {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(mensagem);
        alert.showAndWait();
    }

    default void aviso(@NotNull final AvisoException avisoException) {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(avisoException.getMessage());
        alert.showAndWait();
    }

    default void erro(@NotNull final Throwable throwable) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Erro");
        alert.setHeaderText(throwable.getMessage());

        final Throwable cause = throwable.getCause();
        if (cause != null) {
            alert.setContentText(String.valueOf(cause));
        }

        alert.showAndWait();
    }

    default boolean perguntar(@NotNull final String mensagem) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(mensagem);

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        final Optional<ButtonType> resposta = alert.showAndWait();

        return resposta.isPresent() && resposta.get() == ButtonType.YES;
    }

}
