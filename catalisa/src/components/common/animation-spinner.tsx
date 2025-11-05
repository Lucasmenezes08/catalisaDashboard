export const Spinner = () => {
    return (
        <section className="fixed inset-0 bg-black/70 flex items-center justify-center z-[9999]">
            <section className="bg-white p-8 rounded-lg shadow-xl flex flex-col items-center justify-center w-[150px] h-[150px]">
                <section className="w-[50px] h-[50px] border-[6px] border-gray-200 border-t-[6px] border-t-blue-500 rounded-full animate-spin mb-4"></section>
            </section>
        </section>
    );
};