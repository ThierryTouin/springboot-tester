#!/usr/bin/env bash

function display_help {
  echo
  echo "Usage:"
  echo "  ./cmd.sh COMMAND"
  echo
  echo "Build Commands:"
  echo "  build             => mvn clean install"
  echo "  build-mock        => mvn clean install -Pmock -Dspring.profiles.active=mock."
  echo "                       (le but est de builder et d'executer les TU avec le profil mock)"
  echo "                       (-Dspring.profiles.active=mock est ajouté pour ue cucumber démarre spring avec le profil mock)"
  echo "  build-all-skip    => mvn clean install -DskipTests -Pmock"
  echo
  echo "Test Commands:"
  echo "  test              => mvn clean verify"
  echo "  test-mock         => mvn clean verify -Pmock -Dspring.profiles.active=mock"
  echo
  echo "Start Commands:"
  echo "  start             => mvn spring-boot:run "
  echo "  start-dev         => mvn spring-boot:run -Dspring-boot.run.profiles=dev "
  echo "  start-dev-mock    => mvn spring-boot:run -Dspring-boot.run.profiles=dev,mock "
  echo
  echo "Misc:"
  echo "  help              => Affiche cette aide"
  echo
  exit 1
}

if [ $# -eq 0 ]; then
  display_help
fi

case "$1" in
  build)
    mvn clean install
    ;;
  build-mock)
    mvn clean install -Pmock -Dspring.profiles.active=mock
    ;;
  build-all-skip)
    mvn clean install -DskipTests -Pmock
    ;;
  test)
    mvn clean verify
    ;;
  test-mock)
    mvn clean verify -Pmock -Dspring.profiles.active=mock
    ;;
  start)
    mvn spring-boot:run
    ;;
  start-dev)
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    ;;
  start-dev-mock)
    mvn spring-boot:run -Dspring-boot.run.profiles=dev,mock
    ;;
  help|-h|--help)
    display_help
    ;;
  *)
    echo -e "\e[91mCommande inconnue : $1\e[0m"
    display_help
    ;;
esac
