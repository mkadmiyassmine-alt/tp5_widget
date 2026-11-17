
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(const MaterialApp(
      debugShowCheckedModeBanner: false,
      home: PageWidget(),
    ));

/// Communication avec le code Kotlin
class CanalWidget {
  static const _canal = MethodChannel('widget/maj');

  static Future<int> mettreAJour({
    required double valeur,
    String? titre,
    String? unite,
    double? seuil,
  }) async {
    final n = await _canal.invokeMethod<int>('mettreAJour', {
      'valeur': valeur,
      'titre': titre,
      'unite': unite,
      'seuil': seuil,
    });

    return n ?? 0;
  }

  static Future<int> nombreWidgets() async =>
      await _canal.invokeMethod<int>('nombreWidgets') ?? 0;

  static Future<bool> epingler() async =>
      await _canal.invokeMethod<bool>('epingler') ?? false;
}

class PageWidget extends StatefulWidget {
  const PageWidget({super.key});

  @override
  State<PageWidget> createState() => _PageWidgetState();
}

class _PageWidgetState extends State<PageWidget> {
  double _valeur = 34;
  double _seuil = 38;
  int _nbWidgets = 0;

  @override
  void initState() {
    super.initState();
    _compter();
  }

  Future<void> _compter() async {
    final n = await CanalWidget.nombreWidgets();
    if (!mounted) return;
    setState(() => _nbWidgets = n);
  }

  void _message(String texte) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(texte)),
    );
  }

  Future<void> _envoyer() async {
    try {
      final n = await CanalWidget.mettreAJour(
        titre: 'Ruche · température',
        valeur: _valeur,
        unite: '°C',
        seuil: _seuil,
      );

      if (!mounted) return;
      setState(() => _nbWidgets = n);

      _message(
        n == 0
            ? "Aucun widget sur l'écran d'accueil"
            : 'Envoyé à $n widget(s)',
      );
    } on PlatformException catch (e) {
      _message('Erreur ${e.code} : ${e.message}');
    }
  }

  Future<void> _epingler() async {
    try {
      final ok = await CanalWidget.epingler();

      if (!mounted) return;
      if (!ok) {
        _message(
          "Ajout automatique non pris en charge : ajoutez-le à la main",
        );
      }
    } on PlatformException catch (e) {
      _message('Erreur ${e.code} : ${e.message}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('TP 5 – Widget natif'),
        actions: [
          IconButton(
            onPressed: _compter,
            icon: const Icon(Icons.refresh),
          ),
        ],
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Text("Widgets sur l'écran d'accueil : $_nbWidgets"),
          const SizedBox(height: 8),

          OutlinedButton(
            onPressed: _epingler,
            child: const Text('Ajouter le widget'),
          ),

          const Divider(height: 32),

          Text(
            'Mesure simulée : ${_valeur.toStringAsFixed(1)} °C',
          ),

          Slider(
            value: _valeur,
            min: 20,
            max: 50,
            divisions: 60,
            onChanged: (v) => setState(() => _valeur = v),
          ),

          Text("Seuil d'alerte : ${_seuil.round()} °C"),

          Slider(
            value: _seuil,
            min: 20,
            max: 50,
            divisions: 30,
            onChanged: (v) => setState(() => _seuil = v),
          ),

          const SizedBox(height: 8),

          FilledButton(
            onPressed: _envoyer,
            child: const Text('Envoyer au widget'),
          ),
        ],
      ),
    );
  }
}
