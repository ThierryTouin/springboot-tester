#!/usr/bin/env bash

function display_help {
  echo
  echo "Usage:"
  echo "  ./cmd.sh COMMAND"
  echo
  echo "Build Commands:"
  echo "  build             => mvn clean install"
  echo "  build-skip        => mvn clean install -DskipTests"
  echo "  build-mock        => mvn clean install -DskipTests -Pmock"
  echo
  echo "Test Commands:"
  echo "  test              => mvn clean verify"
  echo "  test-mock         => mvn clean verify -Pmock -Dspring.profiles.active=mock"
  echo
  echo "Start Commands:"
  echo "  start             => mvn spring-boot:run -pl springboot-tester-exposition"
  echo "  start-dev         => mvn spring-boot:run -Dspring-boot.run.profiles=dev -pl springboot-tester-exposition"
  echo "  start-dev-mock    => mvn spring-boot:run -Dspring-boot.run.profiles=dev,mock -pl springboot-tester-exposition"
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
  build-skip)
    mvn clean install -DskipTests
    ;;
  build-mock)
    mvn clean install -DskipTests -Pmock
    ;;
  test)
    mvn clean verify
    ;;
  test-mock)
    mvn clean verify -Pmock -Dspring.profiles.active=mock
    ;;
  start)
    mvn spring-boot:run -pl springboot-tester-exposition
    ;;
  start-dev)
    mvn spring-boot:run -Dspring-boot.run.profiles=dev -pl springboot-tester-exposition
    ;;
  start-dev-mock)
    mvn spring-boot:run -Dspring-boot.run.profiles=dev,mock -pl springboot-tester-exposition
    ;;
  help|-h|--help)
    display_help
    ;;
  *)
    echo -e "\e[91mCommande inconnue : $1\e[0m"
    display_help
    ;;
esac
