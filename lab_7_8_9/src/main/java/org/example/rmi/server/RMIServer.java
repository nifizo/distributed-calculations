package org.example.rmi.server;

import org.example.controller.MergedController;

import java.rmi.Remote;

public interface RMIServer extends Remote, MergedController {
}
