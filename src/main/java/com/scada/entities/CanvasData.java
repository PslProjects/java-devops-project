package com.scada.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CanvasData 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
    @Lob
	private String canvasData;

    public String getCanvasJson() {
        return canvasData;
    }

    public void setCanvasJson(String canvasJson) {
        this.canvasData = canvasJson;
    }
}
