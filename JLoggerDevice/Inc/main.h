/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.h
  * @brief          : Header for main.c file.
  *                   This file contains the common defines of the application.
  ******************************************************************************
  * @attention
  *
  * <h2><center>&copy; Copyright (c) 2019 STMicroelectronics.
  * All rights reserved.</center></h2>
  *
  * This software component is licensed by ST under BSD 3-Clause license,
  * the "License"; You may not use this file except in compliance with the
  * License. You may obtain a copy of the License at:
  *                        opensource.org/licenses/BSD-3-Clause
  *
  ******************************************************************************
  */
/* USER CODE END Header */

/* Define to prevent recursive inclusion -------------------------------------*/
#ifndef __MAIN_H
#define __MAIN_H

#ifdef __cplusplus
extern "C" {
#endif

/* Includes ------------------------------------------------------------------*/
#include "stm32f4xx_hal.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Exported types ------------------------------------------------------------*/
/* USER CODE BEGIN ET */

/* USER CODE END ET */

/* Exported constants --------------------------------------------------------*/
/* USER CODE BEGIN EC */

/* USER CODE END EC */

/* Exported macro ------------------------------------------------------------*/
/* USER CODE BEGIN EM */

/* USER CODE END EM */

void HAL_TIM_MspPostInit(TIM_HandleTypeDef *htim);

/* Exported functions prototypes ---------------------------------------------*/
void Error_Handler(void);

/* USER CODE BEGIN EFP */

/* USER CODE END EFP */

/* Private defines -----------------------------------------------------------*/
#define B1_Pin GPIO_PIN_13
#define B1_GPIO_Port GPIOC
#define Analog_5_Pin GPIO_PIN_0
#define Analog_5_GPIO_Port GPIOC
#define Analog_4_Pin GPIO_PIN_1
#define Analog_4_GPIO_Port GPIOC
#define Digital_25_Pin GPIO_PIN_2
#define Digital_25_GPIO_Port GPIOC
#define Digital_26_Pin GPIO_PIN_3
#define Digital_26_GPIO_Port GPIOC
#define Analog_0_Pin GPIO_PIN_0
#define Analog_0_GPIO_Port GPIOA
#define Analog_1_Pin GPIO_PIN_1
#define Analog_1_GPIO_Port GPIOA
#define USART_TX_Pin GPIO_PIN_2
#define USART_TX_GPIO_Port GPIOA
#define USART_RX_Pin GPIO_PIN_3
#define USART_RX_GPIO_Port GPIOA
#define Analog_2_Pin GPIO_PIN_4
#define Analog_2_GPIO_Port GPIOA
#define PWM_4_Pin GPIO_PIN_5
#define PWM_4_GPIO_Port GPIOA
#define Dither_PWM_Pin GPIO_PIN_6
#define Dither_PWM_GPIO_Port GPIOA
#define Digital_0_Pin GPIO_PIN_7
#define Digital_0_GPIO_Port GPIOA
#define Digital_1_Pin GPIO_PIN_4
#define Digital_1_GPIO_Port GPIOC
#define Digital_2_Pin GPIO_PIN_5
#define Digital_2_GPIO_Port GPIOC
#define Analog_3_Pin GPIO_PIN_0
#define Analog_3_GPIO_Port GPIOB
#define Digital_4_Pin GPIO_PIN_1
#define Digital_4_GPIO_Port GPIOB
#define Digital_5_Pin GPIO_PIN_2
#define Digital_5_GPIO_Port GPIOB
#define Digital_6_Pin GPIO_PIN_10
#define Digital_6_GPIO_Port GPIOB
#define Digital_7_Pin GPIO_PIN_12
#define Digital_7_GPIO_Port GPIOB
#define Digital_8_Pin GPIO_PIN_13
#define Digital_8_GPIO_Port GPIOB
#define Digital_9_Pin GPIO_PIN_14
#define Digital_9_GPIO_Port GPIOB
#define Digital_10_Pin GPIO_PIN_15
#define Digital_10_GPIO_Port GPIOB
#define Digital_11_Pin GPIO_PIN_6
#define Digital_11_GPIO_Port GPIOC
#define Digital_12_Pin GPIO_PIN_7
#define Digital_12_GPIO_Port GPIOC
#define Digital_12C8_Pin GPIO_PIN_8
#define Digital_12C8_GPIO_Port GPIOC
#define Digital_13_Pin GPIO_PIN_9
#define Digital_13_GPIO_Port GPIOC
#define PWM_0_Pin GPIO_PIN_8
#define PWM_0_GPIO_Port GPIOA
#define PWM_1_Pin GPIO_PIN_9
#define PWM_1_GPIO_Port GPIOA
#define PWM_2_Pin GPIO_PIN_10
#define PWM_2_GPIO_Port GPIOA
#define PWM_3_Pin GPIO_PIN_11
#define PWM_3_GPIO_Port GPIOA
#define Digital_14_Pin GPIO_PIN_12
#define Digital_14_GPIO_Port GPIOA
#define TMS_Pin GPIO_PIN_13
#define TMS_GPIO_Port GPIOA
#define TCK_Pin GPIO_PIN_14
#define TCK_GPIO_Port GPIOA
#define Digital_15_Pin GPIO_PIN_15
#define Digital_15_GPIO_Port GPIOA
#define Digital_16_Pin GPIO_PIN_10
#define Digital_16_GPIO_Port GPIOC
#define Digital_17_Pin GPIO_PIN_11
#define Digital_17_GPIO_Port GPIOC
#define Digital_18_Pin GPIO_PIN_12
#define Digital_18_GPIO_Port GPIOC
#define Digital_19_Pin GPIO_PIN_2
#define Digital_19_GPIO_Port GPIOD
#define SWO_Pin GPIO_PIN_3
#define SWO_GPIO_Port GPIOB
#define Digital_20_Pin GPIO_PIN_4
#define Digital_20_GPIO_Port GPIOB
#define Digital_21_Pin GPIO_PIN_5
#define Digital_21_GPIO_Port GPIOB
#define Digital_22_Pin GPIO_PIN_6
#define Digital_22_GPIO_Port GPIOB
#define Digital_23_Pin GPIO_PIN_7
#define Digital_23_GPIO_Port GPIOB
#define Digital_24_Pin GPIO_PIN_8
#define Digital_24_GPIO_Port GPIOB
#define PWM_5_Pin GPIO_PIN_9
#define PWM_5_GPIO_Port GPIOB
/* USER CODE BEGIN Private defines */

/* USER CODE END Private defines */

#ifdef __cplusplus
}
#endif

#endif /* __MAIN_H */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
