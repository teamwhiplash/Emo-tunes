package org.tunes.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.lang.NonNull;

/**
 * Holds the five emotional scores that n8n sends.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class songMetaDTO {

    /** 0‑5 scale – higher = more happy */
    @NonNull
    private Double happiness;

    /** 0‑5 scale – higher = more sad   */
    @NonNull private Double sadness;

    /** 0‑5 scale – higher = more love   */
    @NonNull
    private Double love;

    /** 0‑5 scale – higher = more uplifting */
    @NonNull private Double uplifting;

    /** 0‑5 scale – higher = more rage (rarely used) */
    @NonNull private Double rage;
}
