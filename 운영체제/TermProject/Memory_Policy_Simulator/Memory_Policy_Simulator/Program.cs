using System;
using System.Collections.Generic;

namespace Memory_Policy_Simulator
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("=== Memory Policy Simulator ===");

            // 예: 사용자로부터 페이지 교체 정책 선택받기
            Console.WriteLine("Select page replacement policy:");
            Console.WriteLine("1. FIFO");
            Console.WriteLine("2. LRU");
            Console.WriteLine("3. Optimal");

            Console.Write("Your choice: ");
            string choice = Console.ReadLine();

            switch (choice)
            {
                case "1":
                    Console.WriteLine("Running FIFO simulation...");
                    // FIFO 시뮬레이션 함수 호출
                    break;
                case "2":
                    Console.WriteLine("Running LRU simulation...");
                    // LRU 시뮬레이션 함수 호출
                    break;
                case "3":
                    Console.WriteLine("Running Optimal simulation...");
                    // Optimal 시뮬레이션 함수 호출
                    break;
                default:
                    Console.WriteLine("Invalid choice.");
                    break;
            }

            Console.WriteLine("Simulation complete.");
        }
    }
}