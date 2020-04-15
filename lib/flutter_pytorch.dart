import 'dart:io';
import 'dart:async';
import 'dart:typed_data';
import 'package:meta/meta.dart';
import 'package:path/path.dart';

import 'package:flutter/services.dart';
import 'package:path_provider/path_provider.dart';

class Pytorch {
  static const MethodChannel _channel = const MethodChannel('pytorch');

  static Future<String> _getAbsoluteModelPath(String assetPath, String newName) async {
    Directory directory = await getApplicationDocumentsDirectory();
    String dbPath = join(directory.path, newName);
    ByteData data = await rootBundle.load(assetPath);
    Uint8List bytes = data.buffer.asUint8List();
    //write data to file to have a shareable absolute path
    await File(dbPath).writeAsBytes(bytes);
    return dbPath;
  }

  static Future<int> loadModel({
    @required String modelAsset,
    String labels = ""
  }) async {
    String modelPath = await _getAbsoluteModelPath(modelAsset, "model.pt");
    return await _channel.invokeMethod(
      'loadModel',
      {"model": modelPath, "labels": labels},
    );
  }

  static Future<String> runModelOnImage({
    @required int modelNo,
    @required String imgAsset,
    @required int height,
    @required int width
  }) async {
    String imgPath = await _getAbsoluteModelPath(imgAsset, "test.jpg");
    File file = File(imgPath);
    List byteArray = file.readAsBytesSync();
    return await _channel.invokeMethod(
      'runModelOnImage',
      {
        "modelno": modelNo,
        "img": byteArray,
        "height": height,
        "width": width
      },
    );
  }
}
