package com.sahitya.mezion.model;

import androidx.annotation.NonNull;

import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Reading {
    float x;
    float y;
    float z;

    public Reading(float[] values) {
        this(values[0], values[1], values[2]);
    }

    @Override
    @NonNull
    public String toString() {
        return String.format(Locale.US, "X : %7.3f, Y : %7.3f, Z : %7.3f", x, y, z);
    }
}
