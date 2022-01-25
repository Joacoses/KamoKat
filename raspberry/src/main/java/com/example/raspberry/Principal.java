package com.example.raspberry;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.ByteBuffer;

import static com.example.comun.Mqtt.*;

public class Principal {
    private static MqttClient client;

    public static void main(String[] args){
        System.out.println("¡Hola Raspberry Pi!");
        conectarMqtt();
        long tiempo = System.currentTimeMillis();
        publicarMqtt("tiempo", Long.toHexString(tiempo));
        //publicarMqtt2("tiempoLong", tiempo);

        desconectarMqtt();
    }




    public static void conectarMqtt() {
        try {
            System.out.println("Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topicRoot+"WillTopic","App desconectada".getBytes(),
                    qos, false);
            client.connect(connOpts);
        } catch (MqttException e) {
            System.out.println("Error al conectar." + e);
        }
    }
    public static void publicarMqtt2(String topic,  long mensageLong) {
        try {
            byte[] bytes = ByteBuffer.allocate(Long.SIZE / Byte.SIZE)
                    .putLong(mensageLong).array();
            MqttMessage message = new MqttMessage(bytes);
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot + topic, message);
            System.out.println("Publicando mensaje: " + topic+ "->"+mensageLong);
        } catch (MqttException e) {
            System.out.println("Error al publicar." + e);
        }
    }

    public static void publicarMqtt(String topic, String mensageStr) {
        try {
            MqttMessage message = new MqttMessage(mensageStr.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot + topic, message);
            System.out.println("Publicando mensaje: " + topic+ "->"+mensageStr);
        } catch (MqttException e) {
            System.out.println("Error al publicar." + e);
        }
    }

    public static void suscribirMqtt(String topic, MqttCallback listener) {
        try {
            System.out.println("Suscrito a " + topicRoot + topic);
            client.subscribe(topicRoot + topic, qos);
            client.setCallback(listener);
        } catch (MqttException e) {
            System.out.println("Error al suscribir." + e);
        }
    }


    public static void desconectarMqtt() {
        try {
            client.disconnect();
            System.out.println("Desconectado");
        } catch (MqttException e) {
            System.out.println("Error al desconectar." + e);
        }
    }


}