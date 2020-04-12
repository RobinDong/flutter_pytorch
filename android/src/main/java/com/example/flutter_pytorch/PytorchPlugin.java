package com.example.flutter_pytorch;

import androidx.annotation.NonNull;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.BinaryMessenger;

import java.io.IOException;
import java.util.HashMap;

/** PytorchPlugin */
public class PytorchPlugin implements FlutterPlugin, MethodCallHandler {

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "pytorch");
        BinaryMessenger ctx = flutterPluginBinding.getBinaryMessenger();
        channel.setMethodCallHandler(new PytorchPlugin());
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "pytorch");
        channel.setMethodCallHandler(new PytorchPlugin());
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("loadModel")) {
            try {
                int res = loadModel((HashMap) call.arguments);
                result.success(res);
            } catch (Exception e) {
                result.error("Failed to load model", e.getMessage(), e);
            }
        } /*else if (call.method.equals("runModelOnImage")) {
        try {
        new RunModelOnImage((HashMap) call.arguments, result).executeTfliteTask();
        } catch (Exception e) {
        result.error("Failed to run model", e.getMessage(), e);
        }
        } else if (call.method.equals("runModelOnBinary")) {
        try {
        new RunModelOnBinary((HashMap) call.arguments, result).executeTfliteTask();
        } catch (Exception e) {
        result.error("Failed to run model", e.getMessage(), e);
        }
        }*/ else {
            result.notImplemented();
        }
    }

    private int loadModel(HashMap args) throws IOException {
        String modelPath = args.get("model").toString();
        Module module = Module.load(modelPath);
        if (module == null) {
            return -1;
        }
        return 0;
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    }
}
