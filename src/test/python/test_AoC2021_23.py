#! /usr/bin/env python3
#

import unittest

from AoC2021_23 import Diagram
from AoC2021_23 import Room


class ParseTest(unittest.TestCase):
    def test(self) -> None:
        diagram = Diagram.from_strings(
            (
                "#############",
                "#...........#",
                "###B#C#B#D###",
                "  #D#C#B#A#",
                "  #D#B#A#C#",
                "  #A#D#C#A#",
                "  #########",
            )
        )
        diagram.assert_valid()
        self.assertEqual(diagram.hallway.destination_for, ".")
        self.assertEqual(diagram.room_a.destination_for, "A")
        self.assertEqual(diagram.room_b.destination_for, "B")
        self.assertEqual(diagram.room_c.destination_for, "C")
        self.assertEqual(diagram.room_d.destination_for, "D")
        self.assertEqual(diagram.hallway.capacity, 11)
        self.assertEqual(diagram.room_a.capacity, 4)
        self.assertEqual(diagram.room_b.capacity, 4)
        self.assertEqual(diagram.room_c.capacity, 4)
        self.assertEqual(diagram.room_d.capacity, 4)
        self.assertEqual(diagram.hallway.amphipods, ["."] * 11)
        self.assertEqual(diagram.room_a.amphipods, ["A", "D", "D", "B"])
        self.assertEqual(diagram.room_b.amphipods, ["D", "B", "C", "C"])
        self.assertEqual(diagram.room_c.amphipods, ["C", "A", "B", "B"])
        self.assertEqual(diagram.room_d.amphipods, ["A", "C", "A", "D"])


class RoomTest(unittest.TestCase):
    def test_empty_count(self) -> None:
        self.assertEqual(Room("A", 4, ["A", "A", "A", "A"]).empty_count(), 0)
        self.assertEqual(Room("A", 4, ["A", "A", "A", "."]).empty_count(), 1)
        self.assertEqual(Room("B", 4, ["A", "A", "A", "A"]).empty_count(), 0)

    def test_completeness(self) -> None:
        self.assertEqual(Room("A", 4, ["A", "A", "A", "A"]).completeness(), 4)
        self.assertEqual(Room("A", 4, ["A", "A", "A", "."]).completeness(), 3)
        self.assertEqual(Room("B", 4, ["A", "A", "A", "A"]).completeness(), 0)

    def test_complete(self) -> None:
        self.assertTrue(Room("A", 4, ["A", "A", "A", "A"]).is_complete())
        self.assertFalse(Room("A", 4, ["A", "A", "A", "."]).is_complete())
        self.assertFalse(Room("B", 4, ["A", "A", "A", "A"]).is_complete())

    def test_available_for_move(self) -> None:
        self.assertIsNone(Room("A", 3, ["A", "A", "A"]).available_for_move())
        self.assertIsNone(Room("A", 3, ["A", "A", "."]).available_for_move())
        self.assertIsNone(Room("A", 3, ["A", ".", "."]).available_for_move())
        self.assertIsNone(Room("A", 3, [".", ".", "."]).available_for_move())
        self.assertEqual(Room("A", 3, ["B", "B", "B"]).available_for_move(), 2)
        self.assertEqual(Room("A", 3, ["B", "B", "."]).available_for_move(), 1)
        self.assertEqual(Room("A", 3, ["B", ".", "."]).available_for_move(), 0)
        self.assertEqual(Room("A", 3, ["B", "A", "."]).available_for_move(), 1)
        self.assertEqual(Room("A", 3, ["B", "A", "A"]).available_for_move(), 2)

    def test_vacancy_for(self) -> None:
        self.assertIsNone(Room("A", 3, ["A", "A", "A"]).vacancy_for("A"))
        self.assertIsNone(Room("A", 3, ["A", "A", "B"]).vacancy_for("A"))
        self.assertIsNone(Room("A", 3, ["A", "B", "."]).vacancy_for("A"))
        self.assertIsNone(Room("B", 3, ["A", "B", "."]).vacancy_for("A"))
        self.assertIsNone(Room("B", 3, ["A", "B", "."]).vacancy_for("B"))
        self.assertIsNone(Room("B", 3, [".", ".", "."]).vacancy_for("A"))
        self.assertEqual(Room("A", 3, [".", ".", "."]).vacancy_for("A"), 0)
        self.assertEqual(Room("A", 3, ["A", ".", "."]).vacancy_for("A"), 1)
        self.assertEqual(Room("A", 3, ["A", "A", "."]).vacancy_for("A"), 2)


class DiagramTest(unittest.TestCase):
    def test_empty_in_hallway_left_from(self) -> None:
        diagram1 = Diagram(Room(".", 11, ["."] * 11), None, None, None, None)
        self.assertEqual(diagram1.empty_in_hallway_left_from("room_a"), {0, 1})
        self.assertEqual(
            diagram1.empty_in_hallway_left_from("room_b"), {0, 1, 3}
        )
        self.assertEqual(
            diagram1.empty_in_hallway_left_from("room_c"), {0, 1, 3, 5}
        )
        self.assertEqual(
            diagram1.empty_in_hallway_left_from("room_d"), {0, 1, 3, 5, 7}
        )
        diagram2 = Diagram(
            Room(
                ".",
                11,
                [".", ".", ".", "A", ".", ".", ".", ".", ".", ".", "."],
            ),
            None,
            None,
            None,
            None,
        )
        self.assertEqual(diagram2.empty_in_hallway_left_from("room_a"), {0, 1})
        self.assertEqual(len(diagram2.empty_in_hallway_left_from("room_b")), 0)
        self.assertEqual(diagram2.empty_in_hallway_left_from("room_c"), {5})
        self.assertEqual(diagram2.empty_in_hallway_left_from("room_d"), {5, 7})
        diagram3 = Diagram(
            Room(
                ".",
                11,
                [".", ".", ".", ".", ".", "A", ".", ".", ".", ".", "."],
            ),
            None,
            None,
            None,
            None,
        )
        self.assertEqual(diagram3.empty_in_hallway_left_from("room_a"), {0, 1})
        self.assertEqual(
            diagram3.empty_in_hallway_left_from("room_b"), {0, 1, 3}
        )
        self.assertEqual(len(diagram3.empty_in_hallway_left_from("room_c")), 0)
        self.assertEqual(diagram3.empty_in_hallway_left_from("room_d"), {7})

    def test_empty_in_hallway_rightleft_from(self) -> None:
        diagram1 = Diagram(Room(".", 11, ["."] * 11), None, None, None, None)
        self.assertEqual(
            diagram1.empty_in_hallway_right_from("room_a"), {3, 5, 7, 9, 10}
        )
        self.assertEqual(
            diagram1.empty_in_hallway_right_from("room_b"), {5, 7, 9, 10}
        )
        self.assertEqual(
            diagram1.empty_in_hallway_right_from("room_c"), {7, 9, 10}
        )
        self.assertEqual(
            diagram1.empty_in_hallway_right_from("room_d"), {9, 10}
        )
        diagram2 = Diagram(
            Room(
                ".",
                11,
                [".", ".", ".", ".", ".", "A", ".", ".", ".", ".", "."],
            ),
            None,
            None,
            None,
            None,
        )
        self.assertEqual(diagram2.empty_in_hallway_right_from("room_a"), {3})
        self.assertEqual(
            len(diagram2.empty_in_hallway_right_from("room_b")), 0
        )
        self.assertEqual(
            diagram2.empty_in_hallway_right_from("room_c"), {7, 9, 10}
        )
        self.assertEqual(
            diagram2.empty_in_hallway_right_from("room_d"), {9, 10}
        )

    def test_moves_to_hallway(self) -> None:
        diagram = Diagram.from_strings(
            (
                "#############",
                "#.....D.....#",
                "###C#B#.#C###",
                "  #A#B#D#A#",
                "  #########",
            )
        )
        self.assertEqual(
            diagram.moves_to_hallway(),
            {
                ("room_a", 1, 0),
                ("room_a", 1, 1),
                ("room_a", 1, 3),
                ("room_c", 0, 7),
                ("room_c", 0, 9),
                ("room_c", 0, 10),
                ("room_d", 1, 7),
                ("room_d", 1, 9),
                ("room_d", 1, 10),
            },
        )

    def test_energy_for_move(self) -> None:
        diagram = Diagram.from_strings(
            (
                "#############",
                "#.....D.....#",
                "###C#B#.#A###",
                "  #A#B#D#C#",
                "  #########",
            )
        )
        self.assertEqual(
            diagram.energy_for_move_to_hallway(("room_a", 1, 0)), 300
        )
        self.assertEqual(
            diagram.energy_for_move_to_hallway(("room_a", 1, 1)), 200
        )
        self.assertEqual(
            diagram.energy_for_move_to_hallway(("room_a", 1, 3)), 200
        )
        self.assertEqual(
            diagram.energy_for_move_to_hallway(("room_c", 0, 7)), 3_000
        )
        self.assertEqual(
            diagram.energy_for_move_to_hallway(("room_c", 0, 9)), 5_000
        )
        self.assertEqual(
            diagram.energy_for_move_to_hallway(("room_c", 0, 10)), 6_000
        )
        self.assertEqual(
            diagram.energy_for_move_to_hallway(("room_d", 1, 7)), 2
        )
        self.assertEqual(
            diagram.energy_for_move_to_hallway(("room_d", 1, 9)), 2
        )
        self.assertEqual(
            diagram.energy_for_move_to_hallway(("room_d", 1, 10)), 3
        )

    def test_all_empty(self) -> None:
        diagram = Diagram.from_strings(
            (
                "#############",
                "#...C.D.....#",
                "###.#B#.#A###",
                "  #A#B#D#C#",
                "  #########",
            )
        )
        self.assertTrue(diagram.all_empty(3, "room_a"))
        self.assertTrue(diagram.all_empty(3, "room_b"))
        self.assertTrue(diagram.all_empty(5, "room_b"))
        self.assertTrue(diagram.all_empty(5, "room_c"))
        self.assertTrue(diagram.all_empty(5, "room_d"))
        self.assertFalse(diagram.all_empty(5, "room_a"))
        self.assertFalse(diagram.all_empty(3, "room_c"))
        self.assertFalse(diagram.all_empty(3, "room_d"))

    def test_moves_from_hallway(self) -> None:
        diagram = Diagram.from_strings(
            (
                "#############",
                "#B..A.C...DC#",
                "###.#.#.#.###",
                "  #A#B#D#.#",
                "  #########",
            )
        )
        self.assertEqual(
            diagram.moves_from_hallway(), {("room_a", 3, 1), ("room_d", 9, 0)}
        )

    def test_energy_for_move_from_hallway(self) -> None:
        diagram = Diagram.from_strings(
            (
                "#############",
                "#B..A.C...DC#",
                "###.#.#.#.###",
                "  #A#B#D#.#",
                "  #########",
            )
        )
        self.assertEqual(
            diagram.energy_for_move_from_hallway(("room_a", 3, 1)), 2
        )
        self.assertEqual(
            diagram.energy_for_move_from_hallway(("room_d", 9, 0)), 3_000
        )

    def test_do_move_from_hallway(self) -> None:
        diagram = Diagram.from_strings(
            (
                "#############",
                "#B..A.C...DC#",
                "###.#.#.#.###",
                "  #A#B#D#.#",
                "  #########",
            )
        )
        temp = diagram.do_move_from_hallway(("room_a", 3, 1))
        ans = temp.do_move_from_hallway(("room_d", 9, 0))
        self.assertEqual(
            diagram.hallway.amphipods,
            ["B", ".", ".", "A", ".", "C", ".", ".", ".", "D", "C"],
        )
        self.assertEqual(
            ans.hallway.amphipods,
            ["B", ".", ".", ".", ".", "C", ".", ".", ".", ".", "C"],
        )
        self.assertEqual(diagram.room_a.amphipods, ["A", "."])
        self.assertEqual(ans.room_a.amphipods, ["A", "A"])
        self.assertEqual(diagram.room_b.amphipods, ["B", "."])
        self.assertEqual(ans.room_b.amphipods, ["B", "."])
        self.assertEqual(diagram.room_c.amphipods, ["D", "."])
        self.assertEqual(ans.room_c.amphipods, ["D", "."])
        self.assertEqual(diagram.room_d.amphipods, [".", "."])
        self.assertEqual(ans.room_d.amphipods, ["D", "."])

    def test_do_move_to_hallway(self) -> None:
        diagram = Diagram.from_strings(
            (
                "#############",
                "#.....D.....#",
                "###C#B#.#C###",
                "  #A#B#D#A#",
                "  #########",
            )
        )
        temp = diagram.do_move_to_hallway(("room_a", 1, 0))
        ans = temp.do_move_to_hallway(("room_d", 1, 7))
        self.assertEqual(
            diagram.hallway.amphipods,
            [".", ".", ".", ".", ".", "D", ".", ".", ".", ".", "."],
        )
        self.assertEqual(
            ans.hallway.amphipods,
            ["C", ".", ".", ".", ".", "D", ".", "C", ".", ".", "."],
        )
        self.assertEqual(diagram.room_a.amphipods, ["A", "C"])
        self.assertEqual(ans.room_a.amphipods, ["A", "."])
        self.assertEqual(diagram.room_b.amphipods, ["B", "B"])
        self.assertEqual(ans.room_b.amphipods, ["B", "B"])
        self.assertEqual(diagram.room_c.amphipods, ["D", "."])
        self.assertEqual(ans.room_c.amphipods, ["D", "."])
        self.assertEqual(diagram.room_d.amphipods, ["A", "C"])
        self.assertEqual(ans.room_d.amphipods, ["A", "."])


if __name__ == "__main__":
    unittest.main()
