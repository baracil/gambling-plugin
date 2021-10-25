
import jplugman.api.Plugin;
import perobobbot.gambling.JPlugin;

module perobobbot.gambling {
    requires static lombok;
    requires java.desktop;

    requires jplugman.api;
    requires com.google.common;

    requires perobobbot.oauth;
    requires perobobbot.twitch.client.api;
    requires perobobbot.messaging;
    requires perobobbot.command;
    requires perobobbot.plugin;
    requires perobobbot.extension;
    requires perobobbot.eventsub;
    requires perobobbot.lang;
    requires perobobbot.http;
    requires perobobbot.chat.core;
    requires perobobbot.data.service;
    requires perobobbot.security.com;
    requires perobobbot.access;

    provides Plugin with JPlugin;
}
