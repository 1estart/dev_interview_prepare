myOdds :: [Int] -> [Int]
myOdds xs = filter odd xs

main :: IO ()
main = do
  putStrLn "Hello, everybody!"
  putStrLn ("Please look at my favorite odd numbers: " ++ show (myOdds [10 .. 20]))