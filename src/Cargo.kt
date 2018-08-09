
enum class Cargo {
    /**
     * types of cargo, maybe invalid values
     */
    Alliance{
        override fun toString():String{
            return "AllianceCargo"
        }
        val points = 4
        val StackValue = 1.6
    },Barrel{
        override fun toString():String{
            return "Barrel"
        }
        val points = 6
        val StackValue = 1.4
    },Box{
        override fun toString():String{
            return "Box"
        }
        val points = 8
        val StackValue = 1.8
    },Crate{
        override fun toString():String{
            return "Crate"
        }
        val points = 10
        val StackValue = 2
    },Treasure{
        override fun toString():String{
            return "Treasure"
        }
        val points = 20
        val StackValue = 2.5
    };
}