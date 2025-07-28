Feature: HelloWorld Service

  Scenario: Utiliser le service Hello avec implémentation réelle
    Given le service Hello est prêt
    When j'exécute le service Hello
    Then le résultat est "Hello from real adapter"


