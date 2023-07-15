//package com.example.myapplication;
//
//import bridge from '@vkontakte/vk-bridge';
//
//
//public class VK_Bridge {
//    // Отправляет событие нативному клиенту на инициализацию приложения
//    bridge.send("VKWebAppInit", {});
//    // Подписывается на события, отправленные нативным клиентом
//    bridge.subscribe((e) => console.log(e));
//
//    // Отправляет событие нативному клиенту на инициализацию приложения
//    bridge.send("VKWebAppInit", {});
//
//    // Проверяет, поддерживается ли событие на текущей платформе.
//    if (bridge.supports("VKWebAppResizeWindow")) {
//        bridge.send("VKWebAppResizeWindow", {"width": 800, "height": 1000});
//    }
//    // Sending event to client
//    bridge
//            .send('VKWebAppGetEmail')
//            .then(data => {
//        // Обработка события в случае успеха
//        console.log(data.email);
//    })
//            .catch(error => {
//        // Обработка события в случае ошибки
//    });
//}
